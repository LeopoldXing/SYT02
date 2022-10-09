package com.hilda.yygh.hosp.controller;

import com.alibaba.fastjson.JSON;
import com.hilda.common.result.Result;
import com.hilda.common.utils.HttpRequestHelper;
import com.hilda.yygh.hosp.service.HospitalService;
import com.hilda.yygh.model.hosp.Hospital;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Api("医院管理API接口")
@RestController
@RequestMapping("/api/hosp")
public class ApiController {

    @Autowired
    private HospitalService hospitalService;

    @ApiOperation("添加或更新医院")
    @PostMapping("/saveHospital")
    public Result<Hospital> saveHospital(HttpServletRequest request) {
        //从请求体中取数据
        Map<String, String[]> parameterMap = request.getParameterMap();
        Map<String, Object> map = HttpRequestHelper.switchMap(parameterMap);

        Hospital hospital = JSON.parseObject(JSON.toJSONString(map), Hospital.class);

        if(!hospitalService.saveHospital(hospital)) {
            return Result.fail();
        }

        return Result.ok();
    }

}
