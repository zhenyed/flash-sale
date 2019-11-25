package io.github.zhenyed.api.common.service;

import io.github.zhenyed.api.common.fallback.ProductServiceFallback;
import io.github.zhenyed.api.common.vo.CommonResult;
import io.github.zhenyed.api.product.vo.ProductVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@Api("产品")
@FeignClient(value="flash-sale-product", fallback = ProductServiceFallback.class)
public interface ProductApiService {

    @GetMapping("/product-api/product/info")
    @ApiOperation("产品详情")
    CommonResult<ProductVO> getProductInfo(@RequestParam("id") Integer id);

    @PostMapping("/product-api/product/updatequantity")
    @ApiOperation("更新产品库存")
    CommonResult<Boolean> reduceQuantity(@RequestParam("id") Integer id,
                                         @RequestParam("quantity") Integer quantity);
}
