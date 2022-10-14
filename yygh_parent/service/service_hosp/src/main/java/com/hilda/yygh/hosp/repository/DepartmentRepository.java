package com.hilda.yygh.hosp.repository;

import com.hilda.yygh.model.hosp.Department;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartmentRepository extends MongoRepository<Department, String> {

    /**
     * 根据 医院编号 和 科室编号 查询科室
     * @param hoscode 医院编号
     * @param depcode 科室编号
     * @return 科室对象
     */
    Department getDepartmentByHoscodeAndDepcode(String hoscode, String depcode);

}
