package io.github.zhenyed.api.product;

import io.github.zhenyed.api.product.dataobject.ProductDO;
import io.github.zhenyed.api.product.vo.ProductVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

public class ProductConvert {

    public static ProductVO convert(ProductDO productDO) {
        return new ProductVO()
                .setId(productDO.getId())
                .setName(productDO.getName())
                .setPrice(productDO.getPrice())
                .setQuantity(productDO.getQuantity());
    }
}
