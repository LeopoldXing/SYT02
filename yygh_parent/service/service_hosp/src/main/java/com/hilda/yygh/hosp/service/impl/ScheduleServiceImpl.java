package com.hilda.yygh.hosp.service.impl;

import com.hilda.common.exception.YyghException;
import com.hilda.yygh.hosp.repository.DepartmentRepository;
import com.hilda.yygh.hosp.repository.HospitalRepository;
import com.hilda.yygh.hosp.repository.ScheduleRepository;
import com.hilda.yygh.hosp.service.DepartmentService;
import com.hilda.yygh.hosp.service.HospitalService;
import com.hilda.yygh.hosp.service.ScheduleService;
import com.hilda.yygh.model.hosp.Hospital;
import com.hilda.yygh.model.hosp.Schedule;
import com.hilda.yygh.vo.hosp.BookingScheduleRuleVo;
import com.hilda.yygh.vo.hosp.ScheduleQueryVo;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ScheduleServiceImpl implements ScheduleService {

    @Autowired
    private HospitalRepository hospitalRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private HospitalService hospitalService;

    @Autowired
    private DepartmentService departmentService;

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
        // 分页的排序规则
        Sort sort = Sort.by(Sort.Direction.DESC, "createTime");

        // 设置分页
        Pageable pageable = PageRequest.of(current - 1, size, sort);

        // 查询条件
        Schedule schedule = new Schedule();
        BeanUtils.copyProperties(scheduleQueryVo, schedule);

        // 模糊查询
        ExampleMatcher exampleMatcher = ExampleMatcher.matching()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                .withIgnoreCase(true);

        Example<Schedule> example = Example.of(schedule, exampleMatcher);

        // 分页查询
        Page<Schedule> schedulePage = scheduleRepository.findAll(example, pageable);

        return schedulePage;
    }

    @Override
    public Map<String, Object> showSchedulePage(Integer current, Integer size, String hoscode, String depcode) {
        // 参数验证
        if (current == null || current < 0) current = 1;
        if ((size == null || size <= 0)) size = 7;

        if (hoscode == null || hoscode.length() == 0) throw new YyghException(201, "医院编号为空");
        if (depcode == null || depcode.length() == 0) throw new YyghException(201, "科室编号为空");

        // 分组查询
        // 1. 根据医院编号 和 科室编号 查询
        Criteria criteria = Criteria
                .where("hoscode").is(hoscode)
                .and("depcode").is(depcode);

        // 2. 根据工作日workDate期进行分组
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(criteria),                // 查询条件

                Aggregation.group("workDate")        // 分组字段
                .first("workDate")
                .as("workDate")
                .count().as("docCount")                 // 医生数量
                .sum("reservedNumber").as("reservedNumber")     // 总号源数量
                .sum("availableNumber").as("availableNumber"),  // 剩余号源数量

                // 排序
                Aggregation.sort(Sort.Direction.ASC, "workDate"),

                // 分页
                Aggregation.skip((current - 1) * size),
                Aggregation.limit(size)
        );

        // 3. 调用方法，最终执行
        AggregationResults<BookingScheduleRuleVo> aggregationResults = mongoTemplate.aggregate(aggregation, Schedule.class, BookingScheduleRuleVo.class);

        // 4. 分组结果
        List<BookingScheduleRuleVo> bookingScheduleRuleVoList = aggregationResults.getMappedResults();

        // 5. 分组查询的总记录数
        Aggregation totalAggregation = Aggregation.newAggregation(Aggregation.match(criteria), Aggregation.group("workDate"));
        AggregationResults<BookingScheduleRuleVo> results = mongoTemplate.aggregate(totalAggregation, Schedule.class, BookingScheduleRuleVo.class);
        Integer total = results.getMappedResults().size();

        // 6. 获取日期对应的星期
        bookingScheduleRuleVoList.forEach(bookingScheduleRuleVo -> {
            Date workDate = bookingScheduleRuleVo.getWorkDate();
            String dayOfWeek = this.getDateOfWeek(new DateTime(workDate));
            bookingScheduleRuleVo.setDayOfWeek(dayOfWeek);
        });

        // 7. 获取医院名称
        Hospital hospital = hospitalService.getHospitalByHoscode(hoscode);
        if (hospital == null) throw new YyghException(201, "医院不存在");
        String hospitalName = hospital.getHosname();

        // 8. 封装返回值
        Map<String, Object> res = new HashMap<>();
        Map<String, Object> baseMap = new HashMap<>();

        baseMap.put("hosname", hospitalName);

        res.put("total", total);
        res.put("bookingScheduleRuleList", bookingScheduleRuleVoList);
        res.put("baseMap", baseMap);

        return res;
    }

    @Override
    public List<Schedule> getDetailSchedule(String hoscode, String depcode, String workDate) {
        if (hoscode == null || hoscode.length() == 0) throw new YyghException(201, "医院编号为空");
        if (depcode == null || depcode.length() == 0) throw new YyghException(201, "科室编号为空");

        List<Schedule> scheduleList = scheduleRepository.getSchedulesByHoscodeAndDepcodeAndWorkDate(hoscode, depcode, new DateTime(workDate).toDate());

        scheduleList.parallelStream().forEach(schedule -> packSchedule(schedule, hospitalService, departmentService));

        return scheduleList;
    }

}
