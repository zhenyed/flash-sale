package io.github.zhenyed.api.common.fallback;

import io.github.zhenyed.api.common.service.ProductApiService;
import io.github.zhenyed.api.common.vo.CommonResult;
import io.github.zhenyed.api.product.vo.ProductVO;
import org.springframework.stereotype.Component;

@Component
public class ProductServiceFallback implements ProductApiService {
    @Override
    public CommonResult<ProductVO> getProductInfo(Integer id) {
        return CommonResult.error(666, "fallback");
    }

    @Override
    public CommonResult<Boolean> reduceQuantity(Integer id, Integer quantity) {
        return null;
    }
}
