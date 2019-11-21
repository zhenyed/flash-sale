package io.github.zhenyed.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.github.zhenyed.api.user.dataobject.UserDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<UserDO> {
}
