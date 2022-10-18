package com.hilda.yygh.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hilda.yygh.model.user.UserInfo;

import java.util.Map;

public interface UserInfoService extends IService<UserInfo> {

    Map<String, Object> login(String phone, String code);

}
