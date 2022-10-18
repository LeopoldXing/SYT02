package com.hilda.yygh.hosp.service;

import com.hilda.yygh.model.hosp.Hospital;
import com.hilda.yygh.vo.hosp.HospitalQueryVo;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface HospitalService {

    Page<Hospital> getHospitalByConditionsInPages(Integer current, Integer size, HospitalQueryVo hospitalQueryVo);

    Hospital getHospitalByHoscode(String hoscode);

    Boolean saveHospital(Hospital hospital);

    Boolean updateStatus(String id, Integer status);

    Hospital getHospitalById(String id);

    void packHospital(Hospital hospital);

    List<Hospital> getHospitalByHosname(String hosname);

}
