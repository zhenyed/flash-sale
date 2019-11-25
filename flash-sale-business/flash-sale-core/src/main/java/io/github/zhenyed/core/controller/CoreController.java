package io.github.zhenyed.core.controller;

import io.github.zhenyed.api.auth.CheckLogin;
import io.github.zhenyed.api.common.service.ProductApiService;
import io.github.zhenyed.api.common.vo.CommonResult;
import io.github.zhenyed.api.order.vo.OrderVO;
import io.github.zhenyed.api.order.vo.PreOrderVO;
import io.github.zhenyed.core.service.CoreService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("core")
@Api("秒杀核心")
public class CoreController implements InitializingBean {

    @Autowired
    private CoreService coreService;
    @Autowired
    private RedissonClient redissonClient;

    @CheckLogin
    @PostMapping("create_order")
    @ApiOperation("创建订单")
    public CommonResult<String> createOrder(@RequestParam("productId") Integer productId,
                                             @RequestParam("userId") Integer userId) {
        //Integer userId = UserSecurityContextHolder.getContext().getUserId();
        CommonResult<String> result = coreService.createOrder(userId, productId, 1);
        return result;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        coreService.prepareSetCache();
    }
}
