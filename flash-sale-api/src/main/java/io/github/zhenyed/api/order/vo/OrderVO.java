package io.github.zhenyed.api.order.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.List;

@ApiModel(value = "订单 VO")
@Data
@Accessors(chain = true)
public class OrderVO {

    @ApiModelProperty(value = "订单编号", required = true)
    private Integer id;

    @ApiModelProperty(value = "用户编号", required = true)
    private Integer userId;

    @ApiModelProperty(value = "商品信息", required = true)
    private List<OrderItemVO> orderItems;

    @ApiModelProperty(value = "总数", required = true)
    private Integer total;

    /**
     * 订单状态
     *
     * 0 -- 未支付
     * 1 -- 已支付
     * 2 -- 订单超时
     */
    @ApiModelProperty(value = "订单状态", required = true)
    private Integer status;

    @ApiModelProperty(value = "订单更新时间", required = true)
    private Date createTime;

    @ApiModelProperty(value = "订单创建时间", required = true)
    private Date updateTime;
}
