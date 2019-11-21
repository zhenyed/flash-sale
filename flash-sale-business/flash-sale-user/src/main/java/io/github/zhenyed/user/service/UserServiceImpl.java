package io.github.zhenyed.user.service;

import io.github.zhenyed.api.common.constant.UserErrorCodeEnum;
import io.github.zhenyed.api.common.vo.CommonResult;
import io.github.zhenyed.api.user.UserConvert;
import io.github.zhenyed.api.user.dataobject.UserDO;
import io.github.zhenyed.api.user.vo.UserVO;
import io.github.zhenyed.user.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static io.github.zhenyed.api.common.vo.CommonResult.error;
import static io.github.zhenyed.api.common.vo.CommonResult.success;

@Component
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public CommonResult<UserVO> getUser(Integer id) {
        UserDO userDO = userMapper.selectById(id);
        if (userDO == null) {
            return error(UserErrorCodeEnum.USER_NOT_EXISTS.getCode(),
                         UserErrorCodeEnum.USER_NOT_EXISTS.getMessage());
        }
        return success(UserConvert.INSTANCE.convert(userDO));
    }
}
