package io.github.zhenyed.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.github.zhenyed.api.order.dataobject.OrderDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderMapper extends BaseMapper<OrderDO> {
}
