package com.hilda.yygh.user.controller;

import com.hilda.common.result.R;
import com.hilda.yygh.model.user.Patient;
import com.hilda.yygh.user.service.PatientService;
import com.hilda.yygh.user.utils.AuthContextHolder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Api("患者接口")
@RestController
@RequestMapping("/api/user/patient")
public class PatientController {

    @Autowired
    private PatientService patientService;

    //获取就诊人列表
    @ApiOperation("根据userId获取就诊人列表")
    @GetMapping("/auth/findAll")
    public R getPatientListByUserId(HttpServletRequest request) {
        Long userId = AuthContextHolder.getUserIdFromHttpRequest(request);
        List<Patient> patientListByUserId = patientService.getPatientListByUserId(userId);
        return patientListByUserId == null ? R.error().message("列表为空") : R.ok().data("list", patientListByUserId);
    }

    //添加就诊人
    @ApiOperation("添加就诊人")
    @PostMapping("/auth/save")
    public R savePatient(@RequestBody Patient patient, HttpServletRequest request) {
        Long userId = AuthContextHolder.getUserIdFromHttpRequest(request);
        patient.setUserId(userId);
        return patientService.save(patient) ? R.ok() : R.error();
    }

    //根据id获取就诊人信息
    @ApiOperation("根据就诊人id获取就诊人")
    @GetMapping("/auth/get/{id}")
    public R getPatient(@PathVariable Long id) {
        Patient patientById = patientService.getPatientById(id);
        return patientById == null ? R.error().message("没有指定id的就诊人") : R.ok().data("patient", patientById);
    }

    //修改就诊人
    @ApiOperation("修改就诊人")
    @PostMapping("/auth/update")
    public R updatePatient(@RequestBody Patient patient) {
        return patientService.updateById(patient) ? R.ok() : R.error().message("修改就诊人失败");
    }

    //删除就诊人
    @ApiOperation("根据id删除就诊人")
    @DeleteMapping("/auth/remove/{id}")
    public R removePatient(@PathVariable Long id) {
        return patientService.removeById(id) ? R.ok().message("删除成功") : R.error().message("删除失败");
    }

}
