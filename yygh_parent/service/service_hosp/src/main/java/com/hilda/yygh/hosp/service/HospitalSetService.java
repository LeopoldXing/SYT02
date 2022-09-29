package com.hilda.yygh.hosp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hilda.yygh.model.hosp.HospitalSet;

import java.util.List;

public interface HospitalSetService extends IService<HospitalSet> {

    List<HospitalSet> getAllHospitalSets();

    HospitalSet getHospitalSetById(Long id);

    Boolean addHospitalSet(HospitalSet hospitalSet);

    Boolean deleteHospitalSetById(Long id);

    Boolean deleteHospitalSetByIdList(List<Long> idList);

    Boolean editHospitalSet(HospitalSet hospitalSet);

    Boolean lockHospitalSet(Long id, Integer status);
}
