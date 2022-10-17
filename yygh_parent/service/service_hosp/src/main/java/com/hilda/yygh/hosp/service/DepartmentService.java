package com.hilda.yygh.hosp.service;

import com.hilda.yygh.model.hosp.Department;
import com.hilda.yygh.vo.hosp.DepartmentQueryVo;
import com.hilda.yygh.vo.hosp.DepartmentVo;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;

public interface DepartmentService {

    Boolean saveDepartment(Department department);

    Page<Department> getDepartmentsByConditionsInPages(Integer current, Integer size, DepartmentQueryVo departmentQueryVo);

    Department getDepartmentByDepCodeAndHosCode(String depcode, String hoscode);

    Boolean deleteDepartment(String hoscode, String depcode);

    List<DepartmentVo> getDepartmentListByHoscode(String hoscode);

    default List<DepartmentVo> convertDepartment2DepartmentVo(List<Department> departmentList) {
        List<DepartmentVo> res = new ArrayList<>(departmentList.size());

        departmentList.parallelStream().forEach(department -> {
            DepartmentVo tempDepartmentVo = new DepartmentVo();
            BeanUtils.copyProperties(department, tempDepartmentVo);
            res.add(tempDepartmentVo);
        });

        return res;
    }
}
