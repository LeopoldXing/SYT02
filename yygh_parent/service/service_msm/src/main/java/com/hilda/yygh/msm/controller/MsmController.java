package com.hilda.yygh.msm.controller;

import com.hilda.common.result.R;
import com.hilda.yygh.msm.service.MsmService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;
import java.util.concurrent.TimeUnit;

@Api("验证码接口")
@RestController
@RequestMapping("/api/msm")
public class MsmController {

    // 验证码长度
    private static final int codeLength = 6;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private MsmService msmService;

    @GetMapping("/send/{phone}")
    @ApiOperation("获取验证码")
    public R getMsm(@PathVariable String phone) {
        // 判断5分钟内是否重复发送
        String code = redisTemplate.opsForValue().get(phone);
        if (code != null && code.length() != 0) return R.ok().message("请勿重复发送验证码");

        // 5分钟内第一次发送
        code = generateCode();
        if (msmService.send(phone, code)) {
            // 发送成功
            redisTemplate.opsForValue().set(phone, code, 24, TimeUnit.HOURS);
            return R.ok();
        } else {
            // 发送失败
            return R.error().message("验证码发送失败");
        }
    }

    private String generateCode() {
        String stringCode = String.valueOf((long) (new Random().nextDouble() * Math.pow(10, codeLength)));
        if (stringCode.length() != codeLength) stringCode = generateCode();
        return stringCode;
    }

}
