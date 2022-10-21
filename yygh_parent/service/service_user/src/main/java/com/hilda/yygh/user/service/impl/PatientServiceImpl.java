package com.hilda.yygh.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hilda.common.exception.YyghException;
import com.hilda.yygh.cmn.client.DictFeignClient;
import com.hilda.yygh.model.user.Patient;
import com.hilda.yygh.user.mapper.PatientMapper;
import com.hilda.yygh.user.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

@Service
public class PatientServiceImpl extends ServiceImpl<PatientMapper, Patient> implements PatientService {

    @Autowired
    private PatientMapper patientMapper;

    @Autowired
    private DictFeignClient dictFeignClient;

    @Override
    public List<Patient> getPatientListByUserId(Long id) {
        // 查询
        QueryWrapper<Patient> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", id);
        List<Patient> patientList = patientMapper.selectList(queryWrapper);

        // 查询字典值
        patientList.parallelStream().forEach(this::packPatient);

        return patientList;
    }

    @Override
    public Patient getPatientById(Long id) {
        return packPatient(patientMapper.selectById(id));
    }

    private Patient packPatient(Patient patient) {
        if (patient == null) throw new YyghException(500, "患者对象为空");
        String provinceCodeString = patient.getProvinceCode();
        String cityCodeString = patient.getCityCode();
        String districtCodeString = patient.getDistrictCode();
        String contactsCertificatesTypeString = patient.getContactsCertificatesType();
        String certificatesTypeString = patient.getCertificatesType();

        Map<String, Object> param = patient.getParam();

        if (StringUtils.isEmpty(provinceCodeString)) param.put("provinceString", "");
        else param.put("provinceString", dictFeignClient.getNameByValue(Long.parseLong(provinceCodeString)));
        if (StringUtils.isEmpty(cityCodeString)) param.put("cityString", "");
        else param.put("cityString", dictFeignClient.getNameByValue(Long.parseLong(cityCodeString)));
        if (StringUtils.isEmpty(districtCodeString)) param.put("districtString", "");
        else param.put("districtString", dictFeignClient.getNameByValue(Long.parseLong(districtCodeString)));
        if (StringUtils.isEmpty(contactsCertificatesTypeString)) param.put("contactsCertificatesTypeString", "");
        else param.put("contactsCertificatesTypeString", dictFeignClient.getNameByValue(Long.parseLong(contactsCertificatesTypeString)));
        if (StringUtils.isEmpty(certificatesTypeString)) param.put("certificatesTypeString", "");
        else param.put("certificatesTypeString", dictFeignClient.getNameByValue(Long.parseLong(certificatesTypeString)));
        param.put("fullAddress", provinceCodeString + cityCodeString + districtCodeString + patient.getAddress());

        patient.setParam(param);

        return patient;
    }
}
