package com.hilda.yygh.user.utils;

import com.hilda.common.utils.JwtHelper;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

public class AuthContextHolder {

    public static Long getUserIdFromHttpRequest(HttpServletRequest request) {
        String token = getTokenFromHttpRequest(request);
        if (StringUtils.isEmpty(token)) return 0L;
        else return JwtHelper.getUserId(token);
    }

    public static String getUserNameFromHttpRequest(HttpServletRequest request) {
        String token = getTokenFromHttpRequest(request);
        if (StringUtils.isEmpty(token)) return "";
        else return JwtHelper.getUserName(token);
    }

    public static String getTokenFromHttpRequest(HttpServletRequest request) {
        if (request == null) return "";
        String token = request.getHeader("token");
        return token;
    }

}
