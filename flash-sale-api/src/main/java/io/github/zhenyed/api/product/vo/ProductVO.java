package io.github.zhenyed.api.product.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class ProductVO {

    @ApiModelProperty(value = "商品编号", required = true)
    private Integer id;
    @ApiModelProperty(value = "商品名称", required = true)
    private String name;
    @ApiModelProperty(value = "价格，单位：分", required = true)
    private Integer price;
    @ApiModelProperty(value = "库存数量", required = true)
    private Integer quantity;

}
