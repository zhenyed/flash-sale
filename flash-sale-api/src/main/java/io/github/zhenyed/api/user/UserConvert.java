package io.github.zhenyed.api.user;

import io.github.zhenyed.api.user.dataobject.UserDO;
import io.github.zhenyed.api.user.vo.UserVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserConvert {

    UserConvert INSTANCE = Mappers.getMapper(UserConvert.class);

    @Mappings({})
    UserVO convert(UserDO userDO);
}
