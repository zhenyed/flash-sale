package io.github.zhenyed.api.user.vo;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class LoginVO {
    private TokenVO token;
    private UserVO user;
}
