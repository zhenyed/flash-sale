package io.github.zhenyed.user.service;

import io.github.zhenyed.api.common.vo.CommonResult;
import io.github.zhenyed.api.user.vo.UserVO;

public interface UserService {
    CommonResult<UserVO> getUser(Integer id);
}
