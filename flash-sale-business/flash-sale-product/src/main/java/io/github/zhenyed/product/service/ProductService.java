package io.github.zhenyed.product.service;

import io.github.zhenyed.api.common.vo.CommonResult;
import io.github.zhenyed.api.product.vo.ProductVO;

public interface ProductService {
    CommonResult<ProductVO> getProductInfo(Integer id);

    CommonResult<ProductVO> reduceProductQuantity(Integer id, Integer quantity);
}
