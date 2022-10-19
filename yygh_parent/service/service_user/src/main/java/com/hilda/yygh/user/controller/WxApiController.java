package com.hilda.yygh.user.controller;

import com.hilda.common.result.R;
import com.hilda.yygh.user.service.UserInfoService;
import com.hilda.yygh.user.utils.ConstantPropertiesUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/api/user/wx")
public class WxApiController {

    @Autowired
    private UserInfoService userInfoService;

    //@Autowired
    //private RedisTemplate redisTemplate;

    /**
     * 获取微信登录参数
     */
    @GetMapping("getLoginParam")
    @ResponseBody
    public R genQrConnect() throws UnsupportedEncodingException {

        Map<String, Object> map = new HashMap<>();

        String redirectUri = URLEncoder.encode(ConstantPropertiesUtil.WX_OPEN_REDIRECT_URL, "UTF-8");
        map.put("appid", ConstantPropertiesUtil.WX_OPEN_APP_ID);
        map.put("redirectUri", redirectUri);//确认登录后的重定向地址，并携带code
        map.put("scope", "snsapi_login");
        map.put("state", System.currentTimeMillis()+"");//System.currentTimeMillis()+""

        return R.ok().data(map);
    }

}
