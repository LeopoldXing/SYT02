package com.hilda.yygh.hosp.service.impl;

import com.hilda.common.exception.YyghException;
import com.hilda.yygh.cmn.client.DictFeignClient;
import com.hilda.yygh.enums.DictEnum;
import com.hilda.yygh.hosp.repository.HospitalRepository;
import com.hilda.yygh.hosp.service.HospitalService;
import com.hilda.yygh.model.hosp.Hospital;
import com.hilda.yygh.vo.hosp.HospitalQueryVo;
import net.bytebuddy.implementation.bytecode.Throw;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class HospitalServiceImpl implements HospitalService {

    @Autowired
    private HospitalRepository hospitalRepository;

    @Autowired
    private DictFeignClient dictFeignClient;

    @Override
    public Page<Hospital> getHospitalByConditionsInPages(Integer current, Integer size, HospitalQueryVo hospitalQueryVo) {
        if (current == null || current == 0) current = 1;
        if (size == null || size == 0) size = 10;

        //分页排序
        Sort sort = Sort.by(Sort.Direction.DESC, "createTime");

        //分页设置
        Pageable pageable = PageRequest.of(current - 1, size, sort);

        //准备查询条件
        Hospital hospital = new Hospital();
        BeanUtils.copyProperties(hospitalQueryVo, hospital);

        //模糊查询设置
        ExampleMatcher exampleMatcher = ExampleMatcher.matching()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                .withIgnoreCase(true);

        Example<Hospital> example = Example.of(hospital, exampleMatcher);

        //查询
        Page<Hospital> hospitalPage = hospitalRepository.findAll(example, pageable);

        //将查询结果中的字典编号改为字典名称
        List<Hospital> content = hospitalPage.getContent();
        content.parallelStream().forEach(singleHospitalPage -> {
            packHospital(singleHospitalPage);
        });

        return hospitalPage;
    }

    public void packHospital(Hospital hospital) {
        String hostype = hospital.getHostype();

        String cityCode = hospital.getCityCode();
        String districtCode = hospital.getDistrictCode();
        String provinceCode = hospital.getProvinceCode();

        String cityName = dictFeignClient.getNameByValue(Long.parseLong(cityCode));
        String districtName = dictFeignClient.getNameByValue(Long.parseLong(districtCode));
        String provinceName = dictFeignClient.getNameByValue(Long.parseLong(provinceCode));
        String level = dictFeignClient.getNameByParentDictCodeAndValue(DictEnum.HOSTYPE.getDictCode(), Long.parseLong(hostype));

        hospital.getParam().put("hostypeString", level);
        hospital.getParam().put("fullAddress", provinceName + cityName + districtName + hospital.getAddress());
    }

    @Override
    public Hospital getHospitalByHoscode(String hoscode) {
        if (hoscode == null || hoscode.length() == 0) throw new YyghException(20001, "医院编号为空");

        return hospitalRepository.getHospitalByHoscode(hoscode);
    }

    public Boolean saveHospital(Hospital hospital) {
        hospital.setUpdateTime(new Date());

        String hoscode = hospital.getHoscode();
        if (hoscode == null || hoscode.length() == 0) throw new YyghException(20001, "医院编号为空或不存在");

        Hospital hospitalByHoscode = hospitalRepository.getHospitalByHoscode(hoscode);
        if (hospitalByHoscode == null) {
            //医院不存在
            hospital.setCreateTime(new Date());
        } else {
            //医院已存在
            hospital.setId(hospitalByHoscode.getId());
            hospital.setCreateTime(hospitalByHoscode.getCreateTime());
        }

        return hospitalRepository.save(hospital) != null;
    }

    @Override
    public Boolean updateStatus(String id, Integer status) {
        //验证 id 和 status
        if (id == null || id.length() == 0) throw new YyghException(201, "医院id为空");
        if (status == null || (status != 0 && status != 1)) throw new YyghException(201, "医院状态码不正确");

        //根据id查询医院
        Optional<Hospital> optionalHospital = hospitalRepository.findById(id);
        Hospital hospital = optionalHospital.orElse(null);
        if (hospital == null) throw new YyghException(201, "根据指定id未找到医院");

        //更改医院状态
        hospital.setStatus(status);
        hospital.setUpdateTime(new Date());

        //更新医院
        hospitalRepository.save(hospital);

        return true;
    }

    @Override
    public Hospital getHospitalById(String id) {
        //对医院id进行验证
        if (id == null || id.length() == 0) throw new YyghException(201, "医院id为空");

        Optional<Hospital> optionalHospital = hospitalRepository.findById(id);

        return optionalHospital.orElse(null);
    }

}
