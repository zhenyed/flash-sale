package io.github.zhenyed.api.order.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@ApiModel(value = "订单详情 VO")
@Data
@Accessors(chain = true)
public class OrderItemVO {

    @ApiModelProperty(value = "订单编号", required = true)
    private Integer orderId;

    @ApiModelProperty(value = "产品编号", required = true)
    private Integer productId;

    @ApiModelProperty(value = "订单项数量", required = true)
    private Integer quantity;

    @ApiModelProperty(value = "订单项单价", required = true)
    private Integer singlePrice;

    @ApiModelProperty(value = "订单项总价")
    private Integer totalPrice;

}


