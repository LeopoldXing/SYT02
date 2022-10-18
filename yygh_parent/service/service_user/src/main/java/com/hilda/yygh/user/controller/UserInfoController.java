package com.hilda.yygh.user.controller;

import com.hilda.common.result.R;
import com.hilda.yygh.user.service.UserInfoService;
import com.hilda.yygh.vo.user.LoginVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
