package com.hilda.yygh.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hilda.yygh.model.user.Patient;

import java.util.List;

public interface PatientService extends IService<Patient> {

    List<Patient> getPatientListByUserId(Long id);

    Patient getPatientById(Long id);

}
