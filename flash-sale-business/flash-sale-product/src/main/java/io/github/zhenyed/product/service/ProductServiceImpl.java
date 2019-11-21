package io.github.zhenyed.product.service;

import io.github.zhenyed.api.common.constant.ProductErrorCodeEnum;
import io.github.zhenyed.api.common.vo.CommonResult;
import io.github.zhenyed.api.product.ProductConvert;
import io.github.zhenyed.api.product.dataobject.ProductDO;
import io.github.zhenyed.api.product.vo.ProductVO;
import io.github.zhenyed.product.mapper.ProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import static io.github.zhenyed.api.common.vo.CommonResult.error;
import static io.github.zhenyed.api.common.vo.CommonResult.success;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductMapper productMapper;

    @Override
    public CommonResult<ProductVO> getProductInfo(Integer id) {
        ProductDO productDO = productMapper.selectById(id);
        if (productDO == null) {
            return error(
                    ProductErrorCodeEnum.PRODUCT_SPU_NOT_EXISTS.getCode(),
                    ProductErrorCodeEnum.PRODUCT_SPU_NOT_EXISTS.getMessage()
            );
        }
        return CommonResult.success(ProductConvert.convert(productDO));
    }

    @Override
    public CommonResult<ProductVO> reduceProductQuantity(Integer id, Integer quantity) {
        ProductDO productDO = productMapper.selectById(id);
        if (productDO == null) {
            return error(ProductErrorCodeEnum.PRODUCT_SPU_NOT_EXISTS.getCode(),
                         ProductErrorCodeEnum.PRODUCT_SPU_NOT_EXISTS.getMessage());
        }

        if (productDO.getQuantity() - quantity < 0) {
            return error(ProductErrorCodeEnum.PRODUCT_IN_A_SHORT_INVENTORY.getCode(),
                         ProductErrorCodeEnum.PRODUCT_IN_A_SHORT_INVENTORY.getMessage());
        }

        productDO.setQuantity(productDO.getQuantity() - quantity);
        productMapper.reduceStockById(productDO);
        return success(ProductConvert.convert(productDO));
    }
}
