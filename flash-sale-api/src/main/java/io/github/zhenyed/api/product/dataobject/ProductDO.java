package io.github.zhenyed.api.product.dataobject;

import com.baomidou.mybatisplus.annotation.TableName;
import io.github.zhenyed.api.common.dataobject.DeletableDO;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 商品
 */
@TableName("product")
@Data
@Accessors(chain = true)
public class ProductDO extends DeletableDO {

    private Integer id;

    private String name;
    /**
     * 价格，单位：分
     */
    private Integer price;
    /**
     * 库存数量
     */
    private Integer quantity;

}
