package io.github.zhenyed.order.service;

import io.github.zhenyed.api.common.vo.CommonResult;
import io.github.zhenyed.api.order.vo.OrderVO;
import io.github.zhenyed.api.order.vo.PreOrderVO;

public interface OrderService {
    CommonResult<OrderVO> getOrderInfo(Integer userId, Integer orderId);

    CommonResult<PreOrderVO> createPreOrder(Integer userId, Integer productId, Integer quantity);

    CommonResult<OrderVO> createOrder(Integer userId, Integer productId, Integer quantity);
}
