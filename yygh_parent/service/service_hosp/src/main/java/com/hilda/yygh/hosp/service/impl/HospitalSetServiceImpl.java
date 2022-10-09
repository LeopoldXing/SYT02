package com.hilda.yygh.hosp.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hilda.yygh.hosp.mapper.HospitalRepository;
import com.hilda.yygh.hosp.mapper.HospitalSetMapper;
import com.hilda.yygh.hosp.service.HospitalSetService;
import com.hilda.yygh.model.hosp.HospitalSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HospitalSetServiceImpl extends ServiceImpl<HospitalSetMapper, HospitalSet> implements HospitalSetService {

    @Autowired
    private HospitalSetMapper hospitalSetMapper;

    @Autowired
    private HospitalRepository hospitalRepository;

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
        //判断 id列表 是否为空
        if (idList == null) {
            throw new RuntimeException("给定id列表为空");
        }

        return hospitalSetMapper.deleteBatchIds(idList) > -1;
    }

    @Override
    public Boolean addHospitalSet(HospitalSet hospitalSet) {
        //对传入的 医院设置 进行有效性判断

        //设置医院设置的状态     1 正常    0 锁定
        hospitalSet.setStatus(1);

        int num = hospitalSetMapper.insert(hospitalSet);

        return num > 0;
    }

    @Override
    public Boolean updateHospitalSet(HospitalSet hospitalSet) {
        Boolean res = false;

        if (hospitalSet == null) {
            throw new RuntimeException("医院设置对象为空");
        }

        //判断给定 医院设置是否存在
        Long id = hospitalSet.getId();
        if (id == null || id == 0) {
            //给定医院设置错误
            throw new RuntimeException("医院设置的id为空");
        } else {
            HospitalSet tempHospitalSet = getHospitalSetById(id);
            if (tempHospitalSet == null) {
                //给定 医院设置 不存在
                res = addHospitalSet(hospitalSet);
            } else {
                //给定 医院设置 存在
                hospitalSetMapper.updateById(hospitalSet);
                res = true;
            }
        }

        return res;
    }

    @Override
    public Boolean saveHospitalSet(HospitalSet hospitalSet) {
        //判断 医院设置是否为空
        if (hospitalSet == null) {
            throw new RuntimeException("医院设置对象为空");
        }

        //获取医院设置id
        Long id = hospitalSet.getId();

        //根据id 判断 医院设置 是否存在
        if (id == null || id == 0) {
            //医院设置 不存在，进行插入操作
            return addHospitalSet(hospitalSet);
        } else if (getHospitalSetById(id) != null) {
            //医院设置 存在，进行更新操作
            return updateHospitalSet(hospitalSet);
        } else return false;
    }

    @Override
    public Boolean lockHospitalSet(Long id, Integer status) {
        //判断给定状态码
        if(status == null || status > 1 || status < 0) throw new RuntimeException("给定状态码无效");

        //判断给定id 有效性
        HospitalSet hospitalSet = getHospitalSetById(id);
        if (hospitalSet == null) {
            //医院设置 不存在
            throw new RuntimeException("医院设置对象为空");
        } else {
            //医院设置 存在
            hospitalSet.setStatus(status);
            return updateHospitalSet(hospitalSet);
        }
    }
}
