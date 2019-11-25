package io.github.zhenyed.product.controller;

import io.github.zhenyed.api.auth.CheckLogin;
import io.github.zhenyed.api.common.vo.CommonResult;
import io.github.zhenyed.api.product.vo.ProductVO;
import io.github.zhenyed.product.service.ProductService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("product")
@Api("产品模块")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/info")
    @ApiOperation("商品详情")
    public CommonResult<ProductVO> getProductInfo(@RequestParam("id") Integer id) {
        return productService.getProductInfo(id);
    }

    @CheckLogin
    @PostMapping("/updatequantity")
    @ApiOperation("更新产品库存")
    public CommonResult<Boolean> reduceQuantity(@RequestParam("id") Integer id,
                                           @RequestParam("quantity") Integer quantity) {
        return productService.reduceProductQuantity(id, quantity);
    }
}
