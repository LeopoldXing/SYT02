package com.hilda.yygh.hosp.service.impl;

import com.hilda.common.exception.YyghException;
import com.hilda.yygh.hosp.repository.DepartmentRepository;
import com.hilda.yygh.hosp.repository.HospitalRepository;
import com.hilda.yygh.hosp.service.DepartmentService;
import com.hilda.yygh.hosp.service.HospitalService;
import com.hilda.yygh.model.hosp.Department;
import com.hilda.yygh.model.hosp.Hospital;
import com.hilda.yygh.vo.hosp.DepartmentQueryVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class DepartmentServiceImpl implements DepartmentService {

    @Autowired
    private HospitalRepository hospitalRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    /**
     * 新增 或 修改 科室
     * @param department
     * @return
     */
    public Boolean saveDepartment(Department department) {
        String hoscode = department.getHoscode();
        String depcode = department.getDepcode();
        if(hoscode == null || hoscode.length() == 0) throw new YyghException(20001, "科室所属医院编号为空");
        Hospital hospitalByHoscode = hospitalRepository.getHospitalByHoscode(hoscode);
        if (hospitalByHoscode == null) throw new YyghException(20001, "科室所属医院不存在");
        else if (depcode == null || depcode.length() == 0) throw new YyghException(20001, "科室编号为空");

        department.setUpdateTime(new Date());

        //判断科室是否存在
        Department departmentByHoscodeAndDepcode = departmentRepository.getDepartmentByHoscodeAndDepcode(hoscode, depcode);
        if (departmentByHoscodeAndDepcode == null) {
            //科室不存在
            department.setCreateTime(new Date());
        } else {
            //科室存在
            department.setCreateTime(departmentByHoscodeAndDepcode.getCreateTime());
            department.setId(departmentByHoscodeAndDepcode.getId());
        }

        departmentRepository.save(department);
        return true;
    }

    @Override
    public Page<Department> getDepartmentsByConditionsInPages(Integer current, Integer size, DepartmentQueryVo departmentQueryVo) {
        //分页的排序规则
        Sort sort = Sort.by(Sort.Direction.DESC, "createTime");
        //设置分页
        Pageable pageable = PageRequest.of(current - 1, size, sort);

        //查询条件
        Department department = new Department();
        BeanUtils.copyProperties(departmentQueryVo, department);
        //模糊查询
        ExampleMatcher exampleMatcher = ExampleMatcher.matching()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                .withIgnoreCase(true);

        Example<Department> example = Example.of(department, exampleMatcher);

        //分页查询
        Page<Department> page = departmentRepository.findAll(example, pageable);

        return page;
    }

    @Override
    public Boolean deleteDepartment(String hoscode, String depcode) {
        if (hoscode == null || hoscode.length() == 0 ) throw new YyghException(201, "医院编号为空");
        if (depcode == null || depcode.length() == 0) throw new YyghException(201, "科室编号为空");

        Department departmentByHoscodeAndDepcode = departmentRepository.getDepartmentByHoscodeAndDepcode(hoscode, depcode);
        if (departmentByHoscodeAndDepcode == null) return false;
        else departmentRepository.deleteById(departmentByHoscodeAndDepcode.getId());

        return true;
    }

}
