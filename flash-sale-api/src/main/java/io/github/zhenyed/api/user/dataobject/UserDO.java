package io.github.zhenyed.api.user.dataobject;

import com.baomidou.mybatisplus.annotation.TableName;
import io.github.zhenyed.api.common.dataobject.DeletableDO;
import lombok.Data;
import lombok.experimental.Accessors;

@TableName("user")
@Data
@Accessors(chain = true)
public class UserDO extends DeletableDO {
    private Integer id;
    private String nickname;
}
