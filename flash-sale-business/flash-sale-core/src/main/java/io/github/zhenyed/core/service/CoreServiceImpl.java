package io.github.zhenyed.core.service;

import io.github.zhenyed.api.common.constant.OrderErrorCodeEnum;
import io.github.zhenyed.api.common.service.ProductApiService;
import io.github.zhenyed.api.common.vo.CommonResult;
import io.github.zhenyed.api.constant.CoreConstants;
import io.github.zhenyed.api.product.vo.ProductVO;
import io.github.zhenyed.api.rabbitmq.RabbitConfig;
import org.redisson.api.RQueue;
import org.redisson.api.RScript;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static io.github.zhenyed.api.common.vo.CommonResult.error;
import static io.github.zhenyed.api.common.vo.CommonResult.success;

@Component
public class CoreServiceImpl implements CoreService {

    private static Logger log = LoggerFactory.getLogger(CoreServiceImpl.class);

    private Map<Integer, Boolean> isFinish = new ConcurrentHashMap();

    private static final String DECR_LUA = "if tonumber(redis.call(\"GET\",KEYS[1])) > 0 then\n"+
                                           "     return redis.call(\"DECR\",KEYS[1])\n"+
                                           "   else\n"+
                                           "     return nil\n"+
                                           "   end";

    @Autowired
    private ProductApiService productApiService;
    @Autowired
    private AmqpTemplate amqpTemplate;
    @Autowired
    private RedissonClient redissonClient;


    @Override
    public CommonResult<String> createOrder(Integer userId, Integer productId, Integer quantity) {
        String productStockKey = "product:" + productId + ":stock";
        String productStockLock = "product:" + productId + ":stock:lock";
        String productStockSetKey = "product:" + productId + ":set";
        String productStockList = "product:" + productId + ":liststock";

        if (isFinish.get(productId)) {
            return error(OrderErrorCodeEnum.ORDER_FINISHED.getCode(),
                         OrderErrorCodeEnum.ORDER_FINISHED.getMessage());
        }

        // 1. 方式一获取库存并判断
//        RAtomicDouble stock = redissonClient.getAtomicDouble(productStockKey);
//        if (!stock.isExists() || stock.get() <= 0) {
//            isFinish.put(productId, Boolean.TRUE);
//            return error(OrderErrorCodeEnum.CARD_ITEM_SKU_QUANTITY_NOT_ENOUGH.getCode(),
//                         OrderErrorCodeEnum.CARD_ITEM_SKU_QUANTITY_NOT_ENOUGH.getMessage());
//        }

        // 2. 分布式锁 start
//        redissonClient.getLock(productStockLock).lock(1000, TimeUnit.MILLISECONDS);

        // 3.1. 判断是否已存在订单（能否用 hash 作为已下单的用户）
        boolean exist = redissonClient.getSet(productStockSetKey).contains(userId);
        if (exist) {
            return error(OrderErrorCodeEnum.ORDER_ITEM_ONLY_ONE.getCode(),
                    OrderErrorCodeEnum.ORDER_ITEM_ONLY_ONE.getMessage());
        }

        // 3.2. 方式一减少库存(decr)
//        stock = redissonClient.getAtomicDouble(productStockKey);
//        if (!stock.isExists() || stock.get() < 0 || stock.decrementAndGet() < 0) {
//            isFinish.put(productId, Boolean.TRUE);
//            return error(OrderErrorCodeEnum.CARD_ITEM_SKU_QUANTITY_NOT_ENOUGH.getCode(),
//                    OrderErrorCodeEnum.CARD_ITEM_SKU_QUANTITY_NOT_ENOUGH.getMessage());
//        }
        // 3.2. 方式二减少库存(list)
//        RQueue<Integer> stockQueue = redissonClient.getQueue(productStockList);
//        Integer poll = stockQueue.poll();
//        System.out.println(poll);
//        if (poll == null) {
//            isFinish.put(productId, Boolean.TRUE);
//            return error(OrderErrorCodeEnum.CARD_ITEM_SKU_QUANTITY_NOT_ENOUGH.getCode(),
//                    OrderErrorCodeEnum.CARD_ITEM_SKU_QUANTITY_NOT_ENOUGH.getMessage());
//        }
        // 3.2. 方式三减少库存(lua)
        Long result = redissonClient.getScript()
                .eval(RScript.Mode.READ_WRITE, DECR_LUA, RScript.ReturnType.INTEGER, Arrays.asList(productStockKey), Arrays.asList(productStockKey));
        if (result == null) {
            isFinish.put(productId, Boolean.TRUE);
            return error(OrderErrorCodeEnum.CARD_ITEM_SKU_QUANTITY_NOT_ENOUGH.getCode(),
                    OrderErrorCodeEnum.CARD_ITEM_SKU_QUANTITY_NOT_ENOUGH.getMessage());
        }

        // 3.3. 异步下单
        String asyncCreateOrder = CoreConstants.createOrderMessage(userId, productId);
        amqpTemplate.convertAndSend(RabbitConfig.MIAOSHA_QUEUE, asyncCreateOrder);

        // 3.4. 确定用户已下单
        redissonClient.getSet(productStockSetKey).add(userId);

        // 3. 分布式锁 end兜底
//        redissonClient.getLock(productStockLock).unlock();
        return success(asyncCreateOrder);
    }

    @Override
    public void prepareSetCache() {
        //prepare delete
        redissonClient.getBucket("product:1001:stock").delete();
        redissonClient.getBucket("product:1001:set").delete();
        redissonClient.getBucket("product:1001:liststock").delete();
        isFinish.put(CoreConstants.productId, Boolean.FALSE);

        // 方式一三：Redis Integer decr
        redissonClient.getBucket("product:1001:stock").set(CoreConstants.stock);
        // 方式二： Redis List
//        String productStockList = "product:1001:liststock";
//        for (int i = CoreConstants.stock; i > 0; i--) {
//            redissonClient.getQueue(productStockList).offer(i);
//        }
    }
}
