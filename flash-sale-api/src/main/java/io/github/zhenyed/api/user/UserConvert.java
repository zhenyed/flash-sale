package io.github.zhenyed.api.user;

import io.github.zhenyed.api.user.dataobject.UserDO;
import io.github.zhenyed.api.user.vo.UserVO;

public class UserConvert {

    public static UserVO convert(UserDO userDO) {
        return new UserVO()
                .setId(userDO.getId())
                .setNickname(userDO.getNickname());
    }
}
