package io.github.zhenyed.api.user.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class TokenVO {
    private String token;
    private Long expirationTime;
}
