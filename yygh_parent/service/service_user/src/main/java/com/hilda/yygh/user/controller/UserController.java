package com.hilda.yygh.user.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hilda.common.exception.YyghException;
import com.hilda.common.result.R;
import com.hilda.yygh.model.user.Patient;
import com.hilda.yygh.model.user.UserInfo;
import com.hilda.yygh.user.service.PatientService;
import com.hilda.yygh.user.service.UserInfoService;
import com.hilda.yygh.vo.user.UserInfoQueryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api("后台管理系统用户接口")
@RestController
@RequestMapping("/admin/user")
public class UserController {

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private PatientService patientService;

    //用户列表（条件查询带分页）
    @ApiOperation("分页条件查询 用户列表")
    @GetMapping("/{current}/{size}")
    public R getUserListByConditionsInPages(@PathVariable Integer current, @PathVariable Integer size, UserInfoQueryVo userInfoQueryVo) {
        IPage<UserInfo> userInfoListByConditionsInPages = userInfoService.getUserInfoListByConditionsInPages(userInfoQueryVo, current, size);
        return R.ok().data("pageModel", userInfoListByConditionsInPages);
    }

    @ApiOperation("根据userId锁定或解锁用户")
    @GetMapping("/lock/{userId}/{status}")
    public R lockUserByUserId(@PathVariable Long userId, @PathVariable Integer status) {
        return userInfoService.lockUserByUserId(userId, status) ? R.ok() : R.error();
    }

    @ApiOperation("展示用户详细信息")
    @GetMapping("/show/{userId}")
    public R showUserInfo (@PathVariable Long userId) {
        Map<String, Object> param = new HashMap<>();

        UserInfo userInfo = userInfoService.getUserInfoByUserId(userId);
        param.put("userInfo", userInfo);

        if (userInfo == null) throw new YyghException(203, "用户为空");
        Long id = userInfo.getId();
        List<Patient> patientListByUserId = patientService.getPatientListByUserId(id);

        param.put("patientList", patientListByUserId);

        return R.ok().data(param);
    }

    @ApiOperation("审核用户")
    @GetMapping("/approval/{id}/{authStatus}")
    public R authenticateUser(@PathVariable Long id, @PathVariable Integer authStatus) {
        return userInfoService.authenticate(id, authStatus) ? R.ok() : R.error();
    }

}
