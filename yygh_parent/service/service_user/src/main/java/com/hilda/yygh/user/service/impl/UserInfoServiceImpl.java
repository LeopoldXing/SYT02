package com.hilda.yygh.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hilda.common.exception.YyghException;
import com.hilda.common.utils.JwtHelper;
import com.hilda.yygh.enums.AuthStatusEnum;
import com.hilda.yygh.model.user.UserInfo;
import com.hilda.yygh.user.mapper.UserInfoMapper;
import com.hilda.yygh.user.service.UserInfoService;
import com.hilda.yygh.vo.user.UserAuthVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements UserInfoService {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Override
    public Map<String, Object> login(String phone, String code) {
        // 校验参数
        if (phone == null || phone.length() == 0) throw new YyghException(201, "手机号为空");
        if (code == null || code.length() == 0) throw new YyghException(201, "验证码为空");

        // 验证码校验
        String trueCode = redisTemplate.opsForValue().get(phone);
        if(StringUtils.isEmpty(trueCode)) throw new YyghException(201, "未发送验证码或验证码已过期");
        if(!code.equals(trueCode)) throw new YyghException(201, "验证码不正确");

        // 判断手机号有没有注册
        QueryWrapper<UserInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("phone", phone);
        UserInfo userInfo = userInfoMapper.selectOne(queryWrapper);

        if (userInfo == null) {
            // 该手机号没有注册过
            userInfo = new UserInfo();
            userInfo.setPhone(phone);
            userInfo.setCreateTime(new Date());
            userInfo.setUpdateTime(userInfo.getCreateTime());
            userInfo.setStatus(1);
            if (!this.save(userInfo)) throw new YyghException(201, "用户注册失败");
        }

        // 判断用户有没有被锁定
        if (userInfo.getStatus() == 0) throw new YyghException(201, "用户被锁定");

        // 获取用户名称 和 token
        String name = userInfo.getName();
        if (name == null || name.length() == 0) name = userInfo.getNickName();
        if (name == null || name.length() == 0) name = userInfo.getPhone();

        // 封装需要返回的参数
        Map<String, Object> res = new HashMap<>();
        res.put("name", name);
        res.put("token", JwtHelper.createToken(userInfo.getId(), name));

        return res;
    }

    @Override
    public Boolean authenticate(Long userId, UserAuthVo userAuthVo) {
        //根据userId查询用户
        UserInfo userInfo = userInfoMapper.selectById(userId);

        //更新用户信息对象
        userInfo.setUpdateTime(new Date());
        userInfo.setAuthStatus(AuthStatusEnum.AUTH_RUN.getStatus());
        userInfo.setName(userAuthVo.getName());
        userInfo.setCertificatesNo(userAuthVo.getCertificatesNo());
        userInfo.setCertificatesType(userAuthVo.getCertificatesType());
        userInfo.setCertificatesNo(userAuthVo.getCertificatesNo());
        userInfo.setCertificatesUrl(userAuthVo.getCertificatesUrl());

        //更新用户信息数据库
        int i = userInfoMapper.updateById(userInfo);

        return i == 1;
    }

    @Override
    public UserInfo getUserInfoByUserId(Long userId) {
        if (userId == null || userId == 0) throw new YyghException(202, "用户id为空");

        return userInfoMapper.selectById(userId);
    }
}
