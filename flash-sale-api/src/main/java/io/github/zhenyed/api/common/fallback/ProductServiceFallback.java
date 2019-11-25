package io.github.zhenyed.api.common.fallback;

import io.github.zhenyed.api.common.constant.ProductErrorCodeEnum;
import io.github.zhenyed.api.common.service.ProductApiService;
import io.github.zhenyed.api.common.vo.CommonResult;
import io.github.zhenyed.api.product.vo.ProductVO;
import org.springframework.stereotype.Component;

import static io.github.zhenyed.api.common.vo.CommonResult.error;

@Component
public class ProductServiceFallback implements ProductApiService {
    @Override
    public CommonResult<ProductVO> getProductInfo(Integer id) {
        return error(ProductErrorCodeEnum.PRODUCT_INVOKE_ERROR.getCode(),
                     ProductErrorCodeEnum.PRODUCT_INVOKE_ERROR.getMessage());
    }

    @Override
    public CommonResult<Boolean> reduceQuantity(Integer id, Integer quantity) {
        return error(ProductErrorCodeEnum.PRODUCT_INVOKE_ERROR.getCode(),
                     ProductErrorCodeEnum.PRODUCT_INVOKE_ERROR.getMessage());
    }

//    @Override
//    public CommonResult<Boolean> resetQuantity(Integer id, Integer quantity) {
//        return error(ProductErrorCodeEnum.PRODUCT_INVOKE_ERROR.getCode(),
//                     ProductErrorCodeEnum.PRODUCT_INVOKE_ERROR.getMessage());
//    }
}
