package com.hilda.yygh.hosp.service;

import com.hilda.yygh.model.hosp.Schedule;
import com.hilda.yygh.vo.hosp.ScheduleQueryVo;
import org.springframework.data.domain.Page;

public interface ScheduleService {

    Boolean saveSchedule (Schedule schedule);

    Boolean deleteSchedule(String hoscode, String hosScheduleId);

    Page<Schedule> getScheduleListByConditionsInPages(Integer current, Integer size, ScheduleQueryVo scheduleQueryVo);
}
