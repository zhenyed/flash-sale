package io.github.zhenyed.order.controller;

import io.github.zhenyed.api.auth.CheckLogin;
import io.github.zhenyed.api.common.context.UserSecurityContextHolder;
import io.github.zhenyed.api.common.service.ProductApiService;
import io.github.zhenyed.api.common.vo.CommonResult;
import io.github.zhenyed.api.order.vo.OrderVO;
import io.github.zhenyed.api.order.vo.PreOrderVO;
import io.github.zhenyed.order.service.OrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

/**
 * 	1. 订单详情 [GET]
 *  2. 创建预览订单（不保存信息） [POST]
 * 	3. 确认创建订单 [POST]
 */
@RestController
@RequestMapping("order")
@Api("用户订单")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private ProductApiService productService;

    @GetMapping("test")
    public Object test() {
        return "123: " + productService.getProductInfo(2);
    }

    @CheckLogin
    @GetMapping("info")
    @ApiOperation("订单详情")
    public CommonResult<OrderVO> getOrderInfo(@RequestParam("id") Integer id) {
        Integer userId = 1; //UserSecurityContextHolder.getContext().getUserId();
        CommonResult<OrderVO> commonResult = orderService.getOrderInfo(userId, id);
        return commonResult;
    }

    @CheckLogin
    @PostMapping("create_pre_order")
    @ApiOperation("创建预览订单")
    public CommonResult<PreOrderVO> createPreOrder(@RequestParam("productId") Integer productId,
                                                     @RequestParam("quantity") Integer quantity) {
        Integer userId = 1; //UserSecurityContextHolder.getContext().getUserId();
        CommonResult<PreOrderVO> commonResult = orderService.createPreOrder(userId, productId, quantity);
        return commonResult;
    }

//    @CheckLogin
//    @PostMapping("confirm_create_order")
//    @ApiOperation("创建订单")
//    public CommonResult<OrderVO> createOrder(@RequestParam("productId") Integer productId,
//                                             @RequestParam("userId") Integer userId) {
//        //Integer userId = 1; //UserSecurityContextHolder.getContext().getUserId();
//        CommonResult<OrderVO> orderResult = orderService.createOrder(userId, productId, 1);
//        return orderResult;
//    }
}
