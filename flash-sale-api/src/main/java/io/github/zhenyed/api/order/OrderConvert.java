package io.github.zhenyed.api.order;

import io.github.zhenyed.api.order.dataobject.OrderDO;
import io.github.zhenyed.api.order.dataobject.OrderItemDO;
import io.github.zhenyed.api.order.vo.OrderItemVO;
import io.github.zhenyed.api.order.vo.OrderVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface OrderConvert {

    OrderConvert INSTANCE = Mappers.getMapper(OrderConvert.class);

    @Mappings({})
    OrderVO convert(OrderDO orderDO);

    @Mappings({})
    OrderItemVO convert(OrderItemDO orderItemDO);

    @Mappings({})
    List<OrderItemVO> convert(List<OrderItemDO> orderItems);
}
