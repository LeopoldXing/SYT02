package com.hilda.yygh.hosp.service.impl;

import com.hilda.yygh.hosp.mapper.HospitalRepository;
import com.hilda.yygh.hosp.service.HospitalService;
import com.hilda.yygh.model.hosp.Hospital;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class HospitalServiceImpl implements HospitalService {

    @Autowired
    private HospitalRepository hospitalRepository;

    public Boolean saveHospital(Hospital hospital) {
        //1. 对 hospital 进行有效性判断
        if (hospital == null) {
            throw new RuntimeException("医院为空");
        }

        //2. 查看该医院是否已经存在
        String hoscode = hospital.getHoscode();
        Hospital hospitalByHoscode = hospitalRepository.getHospitalByHoscode(hoscode);
        if (hospitalByHoscode != null) {
            //要插入的医院已存在，更新hospital
            hospital.setId(hospitalByHoscode.getId());
            hospital.setCreateTime(hospitalByHoscode.getCreateTime());
        } else {
            //要插入的医院不存在，插入hospital
            hospital.setCreateTime(new Date());
        }

        Hospital save = hospitalRepository.save(hospital);
        return save != null;
    }

}
