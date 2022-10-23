package com.hilda.yygh.user.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hilda.yygh.model.user.UserInfo;
import com.hilda.yygh.vo.user.UserAuthVo;
import com.hilda.yygh.vo.user.UserInfoQueryVo;

import java.util.Map;

public interface UserInfoService extends IService<UserInfo> {

    IPage<UserInfo> getUserInfoListByConditionsInPages(UserInfoQueryVo userInfoQueryVo, Integer current, Integer size);

    Map<String, Object> login(String phone, String code);

    Boolean applyForAuthentication(Long userId, UserAuthVo userAuthVo);

    UserInfo getUserInfoByUserId(Long userId);

    Boolean lockUserByUserId(Long userId, Integer status);

    Boolean authenticate(Long id, Integer authStatus);

}
