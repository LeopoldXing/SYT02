package com.hilda.yygh.hosp.service.impl;

import com.hilda.common.exception.YyghException;
import com.hilda.yygh.hosp.repository.DepartmentRepository;
import com.hilda.yygh.hosp.repository.HospitalRepository;
import com.hilda.yygh.hosp.repository.ScheduleRepository;
import com.hilda.yygh.hosp.service.ScheduleService;
import com.hilda.yygh.model.hosp.Hospital;
import com.hilda.yygh.model.hosp.Schedule;
import com.hilda.yygh.vo.hosp.ScheduleQueryVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class ScheduleServiceImpl implements ScheduleService {

    @Autowired
    private HospitalRepository hospitalRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Override
    public Boolean saveSchedule(Schedule schedule) {
        String hoscode = schedule.getHoscode();
        if (hoscode == null || hoscode.length() == 0) throw new YyghException(20001, "排班所属医院编号为空或不存在");
        Hospital hospitalByHoscode = hospitalRepository.getHospitalByHoscode(hoscode);
        if (hospitalByHoscode == null) throw new YyghException(20001, "排班所属医院不存在");
        String depcode = schedule.getDepcode();
        if (depcode == null || depcode.length() == 0) throw new YyghException(20001, "排班所属科室编号为空或不存在");
        if (departmentRepository.getDepartmentByHoscodeAndDepcode(hoscode, depcode) == null) throw new YyghException(20001, "排班所属科室不存在");

        schedule.setUpdateTime(new Date());

        //判断排班是否已经存在
        Schedule scheduleByHoscodeAndHosScheduleId = scheduleRepository.getScheduleByHoscodeAndHosScheduleId(hoscode, schedule.getHosScheduleId());
        if (scheduleByHoscodeAndHosScheduleId == null) {
            //排班信息不存在
            schedule.setCreateTime(new Date());
            schedule.setStatus(1);
        } else {
            //排班信息已存在
            schedule.setCreateTime(scheduleByHoscodeAndHosScheduleId.getUpdateTime());
            schedule.setStatus(scheduleByHoscodeAndHosScheduleId.getStatus());
        }

        scheduleRepository.save(schedule);
        return true;
    }

    @Override
    public Boolean deleteSchedule(String hoscode, String hosScheduleId) {
        if (hoscode == null || hoscode.length() == 0) throw new YyghException(201, "医院编号为空");
        if (hosScheduleId == null || hosScheduleId.length() == 0) throw new YyghException(201, "排班编号为空");

        Schedule scheduleByHoscodeAndHosScheduleId = scheduleRepository.getScheduleByHoscodeAndHosScheduleId(hoscode, hosScheduleId);

        if (scheduleByHoscodeAndHosScheduleId == null) {
            return false;
        }
        String id = scheduleByHoscodeAndHosScheduleId.getId();

        scheduleRepository.deleteById(id);

        return true;
    }

    @Override
    public Page<Schedule> getScheduleListByConditionsInPages(Integer current, Integer size, ScheduleQueryVo scheduleQueryVo) {
        //分页的排序规则
        Sort sort = Sort.by(Sort.Direction.DESC, "createTime");

        //设置分页
        Pageable pageable = PageRequest.of(current - 1, size, sort);

        //查询条件
        Schedule schedule = new Schedule();
        BeanUtils.copyProperties(scheduleQueryVo, schedule);

        //模糊查询
        ExampleMatcher exampleMatcher = ExampleMatcher.matching()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                .withIgnoreCase(true);

        Example<Schedule> example = Example.of(schedule, exampleMatcher);

        //分页查询
        Page<Schedule> schedulePage = scheduleRepository.findAll(example, pageable);

        return schedulePage;
    }

}
