package io.github.zhenyed.api.order;

import io.github.zhenyed.api.order.dataobject.OrderDO;
import io.github.zhenyed.api.order.dataobject.OrderItemDO;
import io.github.zhenyed.api.order.vo.OrderItemVO;
import io.github.zhenyed.api.order.vo.OrderVO;

import java.util.ArrayList;
import java.util.List;

public class OrderConvert {

    public static OrderVO convert(OrderDO orderDO) {
        return new OrderVO()
                .setId(orderDO.getId())
                .setUserId(orderDO.getUserId())
                .setUpdateTime(orderDO.getUpdateTime())
                .setCreateTime(orderDO.getCreateTime())
                .setStatus(orderDO.getStatus())
                .setTotal(orderDO.getTotal());
    }

    public static OrderItemVO convert(OrderItemDO orderItemDO) {
        return new OrderItemVO()
                .setOrderId(orderItemDO.getOrderId())
                .setProductId(orderItemDO.getProductId())
                .setQuantity(orderItemDO.getQuantity())
                .setSinglePrice(orderItemDO.getSinglePrice())
                .setTotalPrice(orderItemDO.getTotalPrice());
    }

    public static List<OrderItemVO> convert(List<OrderItemDO> orderItems) {
        List<OrderItemVO> vo = new ArrayList<>();
        for(OrderItemDO itemVO : orderItems) {
            vo.add(OrderConvert.convert(itemVO));
        }
        return vo;
    }
}
