package io.github.zhenyed.api.product;

import io.github.zhenyed.api.product.dataobject.ProductDO;
import io.github.zhenyed.api.product.vo.ProductVO;

public class ProductConvert {

    public static ProductVO convert(ProductDO productDO) {
        return new ProductVO()
                .setId(productDO.getId())
                .setName(productDO.getName())
                .setPrice(productDO.getPrice())
                .setQuantity(productDO.getQuantity());
    }
}
