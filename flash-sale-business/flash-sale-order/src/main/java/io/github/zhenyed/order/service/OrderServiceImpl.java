package io.github.zhenyed.order.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.github.zhenyed.api.common.constant.OrderErrorCodeEnum;
import io.github.zhenyed.api.common.constant.ProductErrorCodeEnum;
import io.github.zhenyed.api.common.service.ProductApiService;
import io.github.zhenyed.api.common.vo.CommonResult;
import io.github.zhenyed.api.constant.CoreConstants;
import io.github.zhenyed.api.order.OrderConvert;
import io.github.zhenyed.api.order.dataobject.OrderDO;
import io.github.zhenyed.api.order.dataobject.OrderItemDO;
import io.github.zhenyed.api.order.vo.OrderItemVO;
import io.github.zhenyed.api.order.vo.OrderVO;
import io.github.zhenyed.api.order.vo.PreOrderVO;
import io.github.zhenyed.api.product.vo.ProductVO;
import io.github.zhenyed.api.rabbitmq.RabbitConfig;
import io.github.zhenyed.order.mapper.OrderItemMapper;
import io.github.zhenyed.order.mapper.OrderMapper;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static io.github.zhenyed.api.common.vo.CommonResult.error;
import static io.github.zhenyed.api.common.vo.CommonResult.success;

@Component
public class OrderServiceImpl implements OrderService {

    private static Logger log = LoggerFactory.getLogger(OrderServiceImpl.class);

    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private OrderItemMapper orderItemMapper;
    @Autowired
    private ProductApiService productApiService;
    @Autowired
    private AmqpTemplate amqpTemplate ;
    @Autowired
    private RedissonClient redissonClient;
//    @Autowired
//    private RedisTemplate redisTemplate;
//    @Autowired
//    private RedisDistributedLock distributedLock;

    @Override
//    @Cacheable(value="order", key="#orderId")
    public CommonResult<OrderVO> getOrderInfo(Integer userId, Integer orderId) {
        OrderDO orderDO = orderMapper.selectOne(
                new QueryWrapper<>(new OrderDO()).eq("user_id", userId).eq("id", orderId));
        if (orderDO == null) {
            return error(OrderErrorCodeEnum.ORDER_NOT_EXISTENT.getCode(),
                         OrderErrorCodeEnum.ORDER_NOT_EXISTENT.getMessage());
        }

        OrderVO orderVO = OrderConvert.convert(orderDO);
        List<OrderItemDO> orderItems = orderItemMapper.selectList(new QueryWrapper<>(new OrderItemDO()).eq("order_id", orderId));
        orderVO.setOrderItems(OrderConvert.convert(orderItems));
        return success(orderVO);
    }

    @Override
    public CommonResult<PreOrderVO> createPreOrder(Integer userId, Integer productId, Integer quantity) {
        ProductVO productInfo = productApiService.getProductInfo(productId).getData();

        if (productInfo == null) {
            return error(ProductErrorCodeEnum.PRODUCT_SPU_NOT_EXISTS.getCode(),
                         ProductErrorCodeEnum.PRODUCT_SPU_NOT_EXISTS.getMessage());
        }

        PreOrderVO preOrderVO = new PreOrderVO();
        OrderItemVO orderItem = new OrderItemVO()
                .setProductId(productId)
                .setQuantity(quantity)
                .setSinglePrice(productInfo.getPrice())
                .setTotalPrice(quantity * productInfo.getPrice());
        preOrderVO.setOrderItems(Arrays.asList(orderItem));
        preOrderVO.setTotal(quantity * productInfo.getPrice());
        preOrderVO.setUserId(userId);

        if (productInfo.getQuantity() < quantity) {
            orderItem.setQuantity(productInfo.getQuantity());
        }

        return success(preOrderVO);
    }

//    @Override
//    public CommonResult<OrderVO> createOrder(Integer userId, Integer productId, Integer quantity) {
//        String productStockKey = "product:" + productId + ":stock";
//        String productStockLock = "product:" + productId + ":stock:lock";
//        String productStockSetKey = "product:" + productId + ":zset";
//        ProductVO productInfo = null;
//
//        // 1. 获取库存并判断
//        RBucket<Integer> stock = redissonClient.getBucket(productStockKey);
////        Integer stock = (Integer) redisTemplate.opsForValue().get(productStockKey);
//        if (!stock.isExists()) {
//            productInfo = productApiService.getProductInfo(productId).getData();
//            if (productInfo == null) {
//                return error(OrderErrorCodeEnum.CARD_ITEM_SKU_NOT_FOUND.getCode(),
//                        OrderErrorCodeEnum.CARD_ITEM_SKU_NOT_FOUND.getMessage());
//            }
//            stock.set(productInfo.getQuantity());
////            redisTemplate.opsForValue().set(productStockKey, productInfo.getQuantity());
//        } else if (stock.get() <= 0) {
//            return error(OrderErrorCodeEnum.CARD_ITEM_SKU_QUANTITY_NOT_ENOUGH.getCode(),
//                         OrderErrorCodeEnum.CARD_ITEM_SKU_QUANTITY_NOT_ENOUGH.getMessage());
//        }
//
//        // 3. 分布式锁
//        redissonClient.getLock(productStockLock).lock(1000, TimeUnit.MILLISECONDS);
////        distributedLock.lock(productStockLock);
//
//        // 2. 判断是否已存在订单（能否用 hash 作为已下单的用户）
//        boolean exist = redissonClient.getSet(productStockSetKey).contains(userId);
////        Boolean exist = redisTemplate.opsForSet().isMember(productStockSetKey, userId);
//        if (exist) {
//            return error(OrderErrorCodeEnum.ORDER_ITEM_ONLY_ONE.getCode(),
//                    OrderErrorCodeEnum.ORDER_ITEM_ONLY_ONE.getMessage());
//        }
//
//        // 3.1 再次获取库存
//        // 3.2. 减少库存
//        double result = redissonClient.getAtomicDouble(productStockKey).decrementAndGet();
////        Long result = redisTemplate.getConnectionFactory().getConnection().decr(redisTemplate.getKeySerializer().serialize(productStockKey));
//        if(result < 0) {
//            return error(OrderErrorCodeEnum.CARD_ITEM_SKU_QUANTITY_NOT_ENOUGH.getCode(),
//                    OrderErrorCodeEnum.CARD_ITEM_SKU_QUANTITY_NOT_ENOUGH.getMessage());
//        }
//
//        // 3.3. 确定用户已下单
//        redissonClient.getSet(productStockSetKey).add(userId);
////        redisTemplate.opsForSet().add(productStockSetKey, userId);
//        // 3.4. 生成订单
////        if (productInfo == null) {
////            productInfo = productApiService.getProductInfo(productId).getData();
////        }
////        OrderDO orderDO = new OrderDO();
////        orderDO.setUserId(userId);
////        orderDO.setTotal(1);
//////        orderDO.setTotal(productInfo.getPrice() * 1);
////        orderDO.setStatus(OrderDO.Status.NOT_PAYMENT.getCode());
////        orderMapper.insert(orderDO);
////
////        // 3.5. 生成订单项
////        OrderDO orderByDb = orderMapper.selectOne(new QueryWrapper<>(orderDO));
////        OrderItemDO orderItemDO = new OrderItemDO()
////                .setOrderId(orderByDb.getId())
////                .setProductId(productId)
////                .setQuantity(quantity)
////                .setSinglePrice(123)
//////                .setSinglePrice(productInfo.getPrice())
////                .setTotalPrice(quantity * productInfo.getPrice());
////        //前提：设置user product 唯一索引，避免两个订单同时生成
////        orderItemMapper.insert(orderItemDO);
////        OrderVO orderVO = OrderConvert.convert(orderByDb);
////        orderVO.setOrderItems(Arrays.asList(OrderConvert.convert(orderItemDO)));
//
//        // 减少 DB 库存(限制库存 > 0)
//        productApiService.reduceQuantity(CoreConstants.productId, 1);
////        productApiService.reduceQuantity(productInfo.getId(), 1);
////        Long result = redisTemplate.opsForSet().add(productStockSetKey, userId);
////        redissonClient.getSet(productStockSetKey).add(userId);
//
//        redissonClient.getLock(productStockLock).unlock();
////        distributedLock.releaseLock(productStockKey);
//        return success(new OrderVO());
//    }

    @RabbitListener(queues=RabbitConfig.MIAOSHA_QUEUE)
    public void receive(String message) {
        log.info("receive message:"+message);
        CoreConstants.CoreModel coreModel = CoreConstants.convertCreateOrderMessage(message);
        Integer productId = coreModel.getProductId();
        Integer userId = coreModel.getUserId();

        // 1. 减少 DB 库存(限制库存 > 0)
        CommonResult<Boolean> reduceResult = productApiService.reduceQuantity(CoreConstants.productId, 1);
        if (reduceResult.isError() || reduceResult.getData() == null || !reduceResult.getData().booleanValue()) {
            return;
        }

        ProductVO productInfo = productApiService.getProductInfo(productId).getData();
        // 2. 生成订单
        OrderDO orderDO = new OrderDO();
        orderDO.setUserId(userId);
        orderDO.setTotal(1);
//        orderDO.setTotal(productInfo.getPrice() * 1);
        orderDO.setStatus(OrderDO.Status.NOT_PAYMENT.getCode());
        orderMapper.insert(orderDO);

        // 3. 生成订单项
//        OrderDO orderByDb = orderMapper.selectOne(new QueryWrapper<>(orderDO));
        OrderItemDO orderItemDO = new OrderItemDO()
                .setOrderId(orderDO.getId())
                .setProductId(productId)
                .setQuantity(1)
                .setSinglePrice(123)
//                .setSinglePrice(productInfo.getPrice())
                .setTotalPrice(1 * productInfo.getPrice());
        //前提：设置user product 唯一索引，避免两个订单同时生成
        orderItemMapper.insert(orderItemDO);
    }
}
