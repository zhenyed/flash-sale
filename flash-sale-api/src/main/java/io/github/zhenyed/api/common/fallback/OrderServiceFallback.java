package io.github.zhenyed.api.common.fallback;

import io.github.zhenyed.api.common.service.OrderApiService;
import io.github.zhenyed.api.common.vo.CommonResult;
import io.github.zhenyed.api.order.vo.OrderVO;
import org.springframework.stereotype.Component;

@Component
public class OrderServiceFallback implements OrderApiService {
    @Override
    public CommonResult<OrderVO> getOrderInfo(Integer orderId) {
        return null;
    }

    @Override
    public CommonResult<OrderVO> createPreOrder(Integer productId, Integer quantity) {
        return null;
    }

    @Override
    public CommonResult<OrderVO> createOrder(Integer productId, Integer quantity) {
        return null;
    }
}
