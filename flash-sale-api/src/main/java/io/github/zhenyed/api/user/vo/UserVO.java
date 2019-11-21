package io.github.zhenyed.api.user.vo;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class UserVO {
    private Integer id;
    private String nickname;
}
