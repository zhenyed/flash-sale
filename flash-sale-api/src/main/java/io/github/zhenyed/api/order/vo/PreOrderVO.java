package io.github.zhenyed.api.order.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@ApiModel(value = "预订单 VO")
@Data
@Accessors(chain = true)
public class PreOrderVO {

    @ApiModelProperty(value = "用户编号", required = true)
    private Integer userId;

    @ApiModelProperty(value = "商品信息", required = true)
    private List<OrderItemVO> orderItems;

    @ApiModelProperty(value = "总数", required = true)
    private Integer total;

}
