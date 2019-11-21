package io.github.zhenyed.api.common.service;

import io.github.zhenyed.api.common.fallback.UserServiceFallback;
import io.swagger.annotations.Api;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api("用户")
@FeignClient(value="flash-sale-user", fallback = UserServiceFallback.class)
public interface UserApiService {

}
