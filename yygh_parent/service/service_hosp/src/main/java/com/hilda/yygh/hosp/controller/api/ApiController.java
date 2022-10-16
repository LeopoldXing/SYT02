package com.hilda.yygh.hosp.controller.api;

import com.alibaba.fastjson.JSON;
import com.hilda.common.exception.YyghException;
import com.hilda.common.result.Result;
import com.hilda.common.utils.HttpRequestHelper;
import com.hilda.common.utils.MD5;
import com.hilda.yygh.hosp.service.DepartmentService;
import com.hilda.yygh.hosp.service.HospitalService;
import com.hilda.yygh.hosp.service.HospitalSetService;
import com.hilda.yygh.hosp.service.ScheduleService;
import com.hilda.yygh.model.hosp.Department;
import com.hilda.yygh.model.hosp.Hospital;
import com.hilda.yygh.model.hosp.Schedule;
import com.hilda.yygh.vo.hosp.DepartmentQueryVo;
import com.hilda.yygh.vo.hosp.ScheduleQueryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
    private HospitalSetService hospitalSetService;

    @Autowired
    private HospitalService hospitalService;

    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private DepartmentService departmentService;

    @ApiOperation("根据编号查找医院")
    @PostMapping("/hospital/show")
    public Result<Hospital> getHospitalByHoscode(HttpServletRequest request) {
        //从请求体中取数据
        Map<String, Object> map = HttpRequestHelper.switchMap(request.getParameterMap());
        String hoscode = String.valueOf(map.get("hoscode"));

        Hospital hospitalByHoscode = hospitalService.getHospitalByHoscode(hoscode);

        return Result.ok(hospitalByHoscode);
    }

    @ApiOperation("添加或更新医院")
    @PostMapping("/saveHospital")
    public Result<Hospital> saveHospital(HttpServletRequest request) {
        //从请求体中取数据
        Map<String, String[]> parameterMap = request.getParameterMap();
        Map<String, Object> map = HttpRequestHelper.switchMap(parameterMap);

        Hospital hospital = JSON.parseObject(JSON.toJSONString(map), Hospital.class);

        String hoscode = hospital.getHoscode();
        if (hoscode == null || hoscode.length() == 0) {
            throw new YyghException(20001,"医院编号为空");
        }

        //签名校验
        //1 获取医院系统传递过来的签名（MD5加密的）
        String encryptedSign = String.valueOf(map.get("sign"));
        if (encryptedSign == null) {
            throw new RuntimeException("签名为空");
        }

        //2 根据传递过来医院编码，查询数据库，查询签名（数据库中是未加密的）
        String unencryptedSignKey = hospitalSetService.getSignKey(hoscode);

        //3 把数据库查询签名进行MD5加密，并进行比对
        if (!encryptedSign.equalsIgnoreCase(MD5.encrypt(unencryptedSignKey))) {
            throw new YyghException(201,"签名校验失败");
        }

        //传输过程中“+”转换为了“ ”，因此我们要转换回来
        String logoData = String.valueOf(map.get("logoData"));
        logoData = logoData.replaceAll(" ","+");
        map.put("logoData",logoData);

        Boolean res = hospitalService.saveHospital(JSON.parseObject(JSON.toJSONString(map), Hospital.class));

        return res ? Result.ok() : Result.fail();
    }

    @ApiOperation("上传科室")
    @PostMapping("/saveDepartment")
    public Result<Department> saveDepartment(HttpServletRequest request) {
        //从请求体中取数据
        Map<String, Object> map = HttpRequestHelper.switchMap(request.getParameterMap());
        Department department = JSON.parseObject(JSON.toJSONString(map), Department.class);

        Boolean res = departmentService.saveDepartment(department);

        return res ? Result.ok() : Result.fail();
    }

    @ApiOperation("分页条件查询科室")
    @PostMapping ("/department/list")
    public Result getDepartmentsByConditionsInPages(HttpServletRequest request) {
        Map<String, Object> map = HttpRequestHelper.switchMap(request.getParameterMap());

        String hoscode = String.valueOf(map.get("hoscode"));
        Integer current = Integer.parseInt(String.valueOf(map.get("page")));
        Integer size = Integer.parseInt(String.valueOf(map.get("limit")));

        /*DepartmentQueryVo departmentQueryVo = JSON.parseObject(JSON.toJSONString(map), DepartmentQueryVo.class);*/
        DepartmentQueryVo departmentQueryVo = new DepartmentQueryVo();
        departmentQueryVo.setHoscode(hoscode);

        Page<Department> departmentPage = departmentService.getDepartmentsByConditionsInPages(current, size, departmentQueryVo);

        return Result.ok(departmentPage);
    }

    @ApiOperation("删除科室")
    @PostMapping("/department/remove")
    public Result deleteDepartment(HttpServletRequest request) {
        Map<String, Object> map = HttpRequestHelper.switchMap(request.getParameterMap());
        String hoscode = String.valueOf(map.get("hoscode"));
        String depcode = String.valueOf(map.get("depcode"));

        departmentService.deleteDepartment(hoscode, depcode);

        return Result.ok();
    }

    @ApiOperation("上传排班")
    @PostMapping("/saveSchedule")
    public Result<Schedule> saveSchedule(HttpServletRequest request) {
        //从请求体中取数据
        Map<String, Object> map = HttpRequestHelper.switchMap(request.getParameterMap());
        Schedule schedule = JSON.parseObject(JSON.toJSONString(map), Schedule.class);

        Boolean res = scheduleService.saveSchedule(schedule);

        return res ? Result.ok() : Result.fail();
    }

    @ApiOperation("分页条件查询排班")
    @PostMapping("/schedule/list")
    public Result<Page<Schedule>> getSchedulesByConditionsInPages (HttpServletRequest request) {
        Map<String, Object> map = HttpRequestHelper.switchMap(request.getParameterMap());

        String stringCurrent = String.valueOf(map.get("page"));
        String stringSize = String.valueOf(map.get("limit"));
        Integer current = (stringCurrent == null || stringCurrent.length() == 0) ? 1 : Integer.parseInt(stringCurrent);
        Integer size = (stringSize == null || stringSize.length() == 0) ? 10 : Integer.parseInt(stringSize);

        ScheduleQueryVo scheduleQueryVo = new ScheduleQueryVo();
        scheduleQueryVo.setHoscode(String.valueOf(map.get("hoscode")));
        scheduleQueryVo.setDepcode(String.valueOf(map.get("depcode")));

        return Result.ok(scheduleService.getScheduleListByConditionsInPages (current, size, scheduleQueryVo));
    }

    @ApiOperation("删除排班")
    @PostMapping("/schedule/remove")
    public Result deleteSchedule (HttpServletRequest request) {
        Map<String, Object> map = HttpRequestHelper.switchMap(request.getParameterMap());
        String hoscode = String.valueOf(map.get("hoscode"));
        String hosScheduleId = String.valueOf(map.get("hosScheduleId"));

        return scheduleService.deleteSchedule(hoscode, hosScheduleId) ? Result.ok() : Result.fail();
    }

}
