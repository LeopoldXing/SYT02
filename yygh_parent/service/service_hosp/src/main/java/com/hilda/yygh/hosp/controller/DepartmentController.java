package com.hilda.yygh.hosp.controller;

import com.hilda.common.result.R;
import com.hilda.yygh.hosp.service.DepartmentService;
import com.hilda.yygh.vo.hosp.DepartmentVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin/hosp/department")
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;

    //根据医院编号，查询医院所有科室列表
    @ApiOperation(value = "查询医院所有科室列表")
    @GetMapping("getDeptList/{hoscode}")
    public R getDepartmentVoList(@PathVariable("hoscode") String hoscode) {
        List<DepartmentVo> departmentListByHoscode = departmentService.getDepartmentListByHoscode(hoscode);

        return R.ok().data("list", departmentListByHoscode);
    }

}
