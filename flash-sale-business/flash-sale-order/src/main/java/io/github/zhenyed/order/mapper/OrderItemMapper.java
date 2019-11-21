package io.github.zhenyed.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.github.zhenyed.api.order.dataobject.OrderItemDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface OrderItemMapper extends BaseMapper<OrderItemDO> {
}
