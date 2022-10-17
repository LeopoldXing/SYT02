package com.hilda.yygh.hosp.service.impl;

import com.hilda.yygh.hosp.service.DepartmentService;
import com.hilda.yygh.vo.hosp.DepartmentVo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class DepartmentServiceTest {

    @Autowired
    private DepartmentService departmentService;

    @Test
    void getDepartmentListByHoscode() {
        List<DepartmentVo> departmentListByHoscode = departmentService.getDepartmentListByHoscode("10000");
        System.out.println(departmentListByHoscode);
    }
}