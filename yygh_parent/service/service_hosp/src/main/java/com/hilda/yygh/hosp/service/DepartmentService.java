package com.hilda.yygh.hosp.service;

import com.hilda.yygh.model.hosp.Department;
import com.hilda.yygh.vo.hosp.DepartmentQueryVo;
import org.springframework.data.domain.Page;

public interface DepartmentService {

    Boolean saveDepartment(Department department);

    Page<Department> getDepartmentsByConditionsInPages(Integer current, Integer size, DepartmentQueryVo departmentQueryVo);

    Boolean deleteDepartment(String hoscode, String depcode);
}
