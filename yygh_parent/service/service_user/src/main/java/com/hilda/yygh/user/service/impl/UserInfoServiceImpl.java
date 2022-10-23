package com.hilda.yygh.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hilda.common.exception.YyghException;
import com.hilda.common.utils.JwtHelper;
import com.hilda.yygh.enums.AuthStatusEnum;
import com.hilda.yygh.model.user.UserInfo;
import com.hilda.yygh.user.mapper.UserInfoMapper;
import com.hilda.yygh.user.service.UserInfoService;
import com.hilda.yygh.vo.user.UserAuthVo;
import com.hilda.yygh.vo.user.UserInfoQueryVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
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
    public IPage<UserInfo> getUserInfoListByConditionsInPages(UserInfoQueryVo userInfoQueryVo, Integer current, Integer size) {
        // 参数校验
        if (current == null || current == 0) current = 1;
        if (size == null || size == 0) size = 10;

        // 分页
        IPage<UserInfo> page = new Page<>(current, size);

        // 参数校验
        if (userInfoQueryVo == null) return userInfoMapper.selectPage(new Page<>(current, size), null);

        // 查询条件
        LambdaQueryWrapper<UserInfo> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(userInfoQueryVo.getStatus() != null, UserInfo::getStatus, userInfoQueryVo.getStatus())
                .eq(userInfoQueryVo.getAuthStatus() != null, UserInfo::getAuthStatus, userInfoQueryVo.getAuthStatus())
                .ge(userInfoQueryVo.getCreateTimeBegin() != null, UserInfo::getCreateTime, userInfoQueryVo.getCreateTimeBegin())
                .le(userInfoQueryVo.getCreateTimeEnd() != null, UserInfo::getCreateTime, userInfoQueryVo.getCreateTimeEnd())
                .like(userInfoQueryVo.getKeyword() != null, UserInfo::getName, userInfoQueryVo.getKeyword());

        // 分页条件查询
        IPage<UserInfo> selectPage = userInfoMapper.selectPage(page, lambdaQueryWrapper);

        selectPage.getRecords().parallelStream().forEach(this::packUserInfo);

        return selectPage;
    }

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
    public Boolean applyForAuthentication(Long userId, UserAuthVo userAuthVo) {
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

    @Override
    public Boolean lockUserByUserId(Long userId, Integer status) {
        if (userId == null || status == null) return false;

        // 判断指定userId的用户是否存在
        UserInfo userInfo = userInfoMapper.selectById(userId);
        if (userInfo == null) throw new YyghException(500, "指定id的用户不存在");

        userInfo.setStatus(status);
        return userInfoMapper.updateById(userInfo) == 1;
    }

    @Override
    public Boolean authenticate(Long id, Integer authStatus) {
        if (id == null || authStatus == null) throw new YyghException(500, "用户id或审核状态码为空");
        if (authStatus != -1 && authStatus != 2) return false;
        UserInfo userInfo = this.getUserInfoByUserId(id);
        if (userInfo == null) return false;

        userInfo.setAuthStatus(authStatus);
        return userInfoMapper.updateById(userInfo) == 1;
    }

    public UserInfo packUserInfo(UserInfo userInfo) {
        if (userInfo == null) throw new YyghException(500, "用户为空");
        //处理认证状态编号
        userInfo.getParam().put("authStatusString",AuthStatusEnum.getStatusNameByStatus(userInfo.getAuthStatus()));

        //处理用户状态 0  1
        userInfo.getParam().put("statusString", userInfo.getStatus() == 0 ? "锁定" : "正常");

        return userInfo;
    }
}
