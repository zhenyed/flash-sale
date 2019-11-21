package io.github.zhenyed.api.common.service;

import io.github.zhenyed.api.common.fallback.OrderServiceFallback;
import io.github.zhenyed.api.common.vo.CommonResult;
import io.github.zhenyed.api.order.vo.OrderVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@Api("用户订单")
@FeignClient(value="flash-sale-order", fallback = OrderServiceFallback.class)
public interface OrderApiService {

    @GetMapping("info")
    @ApiOperation("订单详情")
    CommonResult<OrderVO> getOrderInfo(@RequestParam("orderId") Integer orderId);

    @PostMapping("create_pre_order")
    @ApiOperation("创建预览订单")
    CommonResult<OrderVO> createPreOrder(@RequestParam("productId") Integer productId,
                                                @RequestParam("quantity") Integer quantity);

    @PostMapping("confirm_create_order")
    @ApiOperation("创建订单")
    CommonResult<OrderVO> createOrder(@RequestParam("productId") Integer productId,
                                             @RequestParam("quantity") Integer quantity);
}

