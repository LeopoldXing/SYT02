package com.hilda.yygh.user.controller;

import com.hilda.common.result.R;
import com.hilda.yygh.enums.AuthStatusEnum;
import com.hilda.yygh.model.user.UserInfo;
import com.hilda.yygh.user.service.UserInfoService;
import com.hilda.yygh.user.utils.AuthContextHolder;
import com.hilda.yygh.vo.user.LoginVo;
import com.hilda.yygh.vo.user.UserAuthVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Api("用户接口")
@RestController
@RequestMapping("/api/user")
public class UserInfoController {

    @Autowired
    private UserInfoService userInfoService;

    @ApiOperation("登录")
    @PostMapping("/login")
    public R login(@RequestBody LoginVo loginVo) {
        if (loginVo == null) return R.error().code(201).message("loginVo对象为空");
        String phone = loginVo.getPhone();
        String code = loginVo.getCode();

        return R.ok().data(userInfoService.login(phone, code));
    }

    @ApiOperation("用户认证接口")
    @PostMapping("/auth/userAuth")
    public R userAuthenticate(@RequestBody UserAuthVo userAuthVo, HttpServletRequest request) {
        // 查询 userId
        Long userId = AuthContextHolder.getUserIdFromHttpRequest(request);

        return userInfoService.applyForAuthentication(userId, userAuthVo) ? R.ok() : R.error().message("认证操作失败");
    }

    @ApiOperation("根据id查询用户信息")
    @GetMapping("/auth/getUserInfo")
    public R getUserInfoById(HttpServletRequest request) {
        // 查询 userId
        Long userId = AuthContextHolder.getUserIdFromHttpRequest(request);
        // 根据userId查询用户信息
        UserInfo userInfo = userInfoService.getUserInfoByUserId(userId);

        // 获取用户认证状态
        Integer authStatus = userInfo.getAuthStatus();
        userInfo.getParam().put("authStatusString", AuthStatusEnum.getStatusNameByStatus(authStatus));

        return R.ok().data("userInfo", userInfo);
    }

}
