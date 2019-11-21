package io.github.zhenyed.user.controller;

import io.github.zhenyed.api.auth.CheckLogin;
import io.github.zhenyed.api.auth.JwtOperator;
import io.github.zhenyed.api.common.vo.CommonResult;
import io.github.zhenyed.api.user.vo.LoginVO;
import io.github.zhenyed.api.user.vo.TokenVO;
import io.github.zhenyed.api.user.vo.UserVO;
import io.github.zhenyed.user.service.UserService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

import static io.github.zhenyed.api.common.vo.CommonResult.error;
import static io.github.zhenyed.api.common.vo.CommonResult.success;

@RequestMapping("/user")
@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtOperator jwtOperator;

    @CheckLogin
    @GetMapping("/info/{id}")
    @ApiOperation(value = "用户信息")
    public CommonResult<UserVO> info(@PathVariable("id") Integer id) {
        CommonResult<UserVO> userResult = userService.getUser(id);
        return userResult;
    }

    @PostMapping("/login")
    @ApiOperation(value = "用户登陆")
    public CommonResult<LoginVO> login(@RequestParam("id") Integer userId) {
        CommonResult<UserVO> userResult = userService.getUser(userId);
        if (userResult.isError()) {
            return error(userResult);
        }

        // 颁发token
        Map<String, Object> userInfo = new HashMap<>(1);
        userInfo.put("userId", userResult.getData().getId());
        userInfo.put("nickname", userResult.getData().getNickname());
        String token = jwtOperator.generateToken(userInfo);

        LoginVO loginVO = new LoginVO()
                    .setToken(new TokenVO()
                            .setToken(token)
                            .setExpirationTime(jwtOperator.getExpirationTime().getTime()))
                    .setUser(new UserVO()
                            .setId(userResult.getData().getId())
                            .setNickname(userResult.getData().getNickname())
                    );
        return success(loginVO);
    }
}
