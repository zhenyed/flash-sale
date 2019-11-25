package io.github.zhenyed.product.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.github.zhenyed.api.product.dataobject.ProductDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface ProductMapper extends BaseMapper<ProductDO> {

    @Update("UPDATE product SET quantity = quantity - 1 WHERE id = #{id} and quantity > 0;")
    int reduceStockById(ProductDO productDO);
}
