package io.github.zhenyed.api.product;

import io.github.zhenyed.api.product.dataobject.ProductDO;
import io.github.zhenyed.api.product.vo.ProductVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper//(componentModel = "spring")
public interface ProductConvert {

    ProductConvert INSTANCE = Mappers.getMapper(ProductConvert.class);

//    @Mappings({
//            @Mapping(target="id", source="productDO.id"),
//            @Mapping(target="name", source="productDO.name"),
//            @Mapping(target="price", source="productDO.price"),
//            @Mapping(target="quantity", source="productDO.quantity")
//    })
    @Mappings({})
    ProductVO convert(ProductDO productDO);
}
