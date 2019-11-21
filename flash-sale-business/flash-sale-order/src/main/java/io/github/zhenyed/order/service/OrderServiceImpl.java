package io.github.zhenyed.order.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.github.zhenyed.api.common.constant.OrderErrorCodeEnum;
import io.github.zhenyed.api.common.constant.ProductErrorCodeEnum;
import io.github.zhenyed.api.common.service.ProductApiService;
import io.github.zhenyed.api.common.vo.CommonResult;
import io.github.zhenyed.api.order.OrderConvert;
import io.github.zhenyed.api.order.dataobject.OrderDO;
import io.github.zhenyed.api.order.dataobject.OrderItemDO;
import io.github.zhenyed.api.order.vo.OrderItemVO;
import io.github.zhenyed.api.order.vo.OrderVO;
import io.github.zhenyed.api.order.vo.PreOrderVO;
import io.github.zhenyed.api.product.vo.ProductVO;
import io.github.zhenyed.api.lock.RedisDistributedLock;
import io.github.zhenyed.order.mapper.OrderItemMapper;
import io.github.zhenyed.order.mapper.OrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

import static io.github.zhenyed.api.common.vo.CommonResult.error;
import static io.github.zhenyed.api.common.vo.CommonResult.success;

@Component
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private OrderItemMapper orderItemMapper;
    @Autowired
    private ProductApiService productApiService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private RedisDistributedLock distributedLock;

    @Override
    @Cacheable(value="order", key="#orderId")
    public CommonResult<OrderVO> getOrderInfo(Integer userId, Integer orderId) {
        OrderDO orderDO = orderMapper.selectOne(
                new QueryWrapper<>(new OrderDO()).eq("user_id", userId).eq("id", orderId));
        if (orderDO == null) {
            return error(OrderErrorCodeEnum.ORDER_NOT_EXISTENT.getCode(),
                         OrderErrorCodeEnum.ORDER_NOT_EXISTENT.getMessage());
        }

        OrderVO orderVO = OrderConvert.INSTANCE.convert(orderDO);
        List<OrderItemDO> orderItems = orderItemMapper.selectList(new QueryWrapper<>(new OrderItemDO()).eq("order_id", orderId));
        orderVO.setOrderItems(OrderConvert.INSTANCE.convert(orderItems));
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

    @Override
    @Transactional
    public CommonResult<OrderVO> createOrder(Integer userId, Integer productId, Integer quantity) {
        String productStockKey = "product:" + productId + ":stock";
        String productStockLock = "product:" + productId + ":stock:lock";
        String productStockSetKey = "product:" + productId + ":zset";
        ProductVO productInfo = null;

        // 1. 获取库存并判断
        Integer stock = (Integer) redisTemplate.opsForValue().get(productStockKey);
        if (stock == null) {
            productInfo = productApiService.getProductInfo(productId).getData();
            if (productInfo == null) {
                return error(OrderErrorCodeEnum.CARD_ITEM_SKU_NOT_FOUND.getCode(),
                        OrderErrorCodeEnum.CARD_ITEM_SKU_NOT_FOUND.getMessage());
            }
            redisTemplate.opsForValue().set(productStockKey, productInfo.getQuantity());
        } else if (stock == 0) {
            return error(OrderErrorCodeEnum.CARD_ITEM_SKU_QUANTITY_NOT_ENOUGH.getCode(),
                         OrderErrorCodeEnum.CARD_ITEM_SKU_QUANTITY_NOT_ENOUGH.getMessage());
        }

        // 2. 判断是否已存在订单（能否用 hash 作为已下单的用户）
        Boolean member = redisTemplate.opsForSet().isMember(productStockSetKey, userId);
        if (member) {
            return error(OrderErrorCodeEnum.ORDER_ITEM_ONLY_ONE.getCode(),
                    OrderErrorCodeEnum.ORDER_ITEM_ONLY_ONE.getMessage());
        }

        // 3. 分布式锁
        distributedLock.lock(productStockLock);
        // 3.1 再次获取库存
        stock = (Integer) redisTemplate.opsForValue().get(productStockKey);
        if(stock == null ||stock.intValue() <= 0) {
            return error(OrderErrorCodeEnum.CARD_ITEM_SKU_QUANTITY_NOT_ENOUGH.getCode(),
                    OrderErrorCodeEnum.CARD_ITEM_SKU_QUANTITY_NOT_ENOUGH.getMessage());
        }

        // 3.2. 减少库存
        redisTemplate.getConnectionFactory().getConnection().decr(redisTemplate.getKeySerializer().serialize(productStockKey));
        // 3.3. 确定用户已下单
        redisTemplate.opsForSet().add(productStockSetKey, userId);
        // 3.4. 生成订单
        if (productInfo == null) {
            productInfo = productApiService.getProductInfo(productId).getData();
        }
        OrderDO orderDO = new OrderDO();
        orderDO.setUserId(userId);
        orderDO.setTotal(productInfo.getPrice() * 1);
        orderDO.setStatus(OrderDO.Status.NOT_PAYMENT.getCode());
        orderMapper.insert(orderDO);

        // 3.5. 生成订单项
        OrderDO orderByDb = orderMapper.selectOne(new QueryWrapper<>(orderDO));
        OrderItemDO orderItemDO = new OrderItemDO()
                .setOrderId(orderByDb.getId())
                .setProductId(productId)
                .setQuantity(quantity)
                .setSinglePrice(productInfo.getPrice())
                .setTotalPrice(quantity * productInfo.getPrice());
        //前提：设置user product 唯一索引，避免两个订单同时生成
        orderItemMapper.insert(orderItemDO);
        OrderVO orderVO = OrderConvert.INSTANCE.convert(orderByDb);
        orderVO.setOrderItems(Arrays.asList(OrderConvert.INSTANCE.convert(orderItemDO)));

        // 减少 DB 库存(限制库存 > 0)
        productApiService.reduceQuantity(productInfo.getId(), 1);
        Long result = redisTemplate.opsForSet().add(productStockSetKey, userId);

        distributedLock.releaseLock(productStockKey);
        return success(orderVO);
    }
}
