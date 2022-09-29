package com.hilda.yygh.hosp.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hilda.yygh.hosp.mapper.HospitalSetMapper;
import com.hilda.yygh.hosp.service.HospitalSetService;
import com.hilda.yygh.model.hosp.HospitalSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class HospitalSetServiceImpl extends ServiceImpl<HospitalSetMapper, HospitalSet> implements HospitalSetService {

    @Autowired
    private HospitalSetMapper hospitalSetMapper;

    @Override
    public List<HospitalSet> getAllHospitalSets() {
        return hospitalSetMapper.selectList(null);
    }

    @Override
    public HospitalSet getHospitalSetById(Long id) {
        if (id == null) {
            return null;
        }
        return hospitalSetMapper.selectById(id);
    }

    @Override
    public Boolean deleteHospitalSetById(Long id) {
        //判断给定id是否存在 医院设置
        HospitalSet hospitalSet = getHospitalSetById(id);
        if (hospitalSet == null) {
            //不存在
            return true;
        } else {
            //存在
            return hospitalSetMapper.deleteById(id) > 0;
        }
    }

    @Override
    public Boolean deleteHospitalSetByIdList(List<Long> idList) {
        boolean res = true;

        //判断 id列表 有效性
        if (idList == null) {
            return false;
        }

        //遍历 id列表
        for (Long id : idList) {
            if (!deleteHospitalSetById(id)) res = false;
        }

        return res;
    }

    @Override
    public Boolean addHospitalSet(HospitalSet hospitalSet) {
        //对传入的 医院设置 进行有效性判断

        hospitalSet.setCreateTime(new Date());
        hospitalSet.setUpdateTime(new Date());
        int num = hospitalSetMapper.insert(hospitalSet);

        return num > 0;
    }

    @Override
    public Boolean editHospitalSet(HospitalSet hospitalSet) {
        Boolean res = false;

        //判断给定 医院设置是否存在
        Long id = hospitalSet.getId();
        if (id == null) {
            //给定医院设置错误
            return false;
        } else {
            HospitalSet tempHospitalSet = getHospitalSetById(id);
            if (tempHospitalSet == null) {
                //给定 医院设置 不存在
                res = addHospitalSet(hospitalSet);
            } else {
                //给定 医院设置 存在
                hospitalSet.setUpdateTime(new Date());
                hospitalSetMapper.updateById(hospitalSet);
                res = true;
            }
        }

        return res;
    }

    @Override
    public Boolean lockHospitalSet(Long id, Integer status) {
        //判断给定id 有效性
        HospitalSet hospitalSet = getHospitalSetById(id);
        if (hospitalSet == null) {
            //医院设置 不存在
            return false;
        } else {
            //医院设置 存在
            hospitalSet.setStatus(status);
            return editHospitalSet(hospitalSet);
        }
    }
}
