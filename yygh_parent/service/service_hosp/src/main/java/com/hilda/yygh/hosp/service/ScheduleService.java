package com.hilda.yygh.hosp.service;

import com.hilda.yygh.model.hosp.Schedule;
import com.hilda.yygh.vo.hosp.ScheduleQueryVo;
import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.springframework.data.domain.Page;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface ScheduleService {

    Boolean saveSchedule (Schedule schedule);

    Boolean deleteSchedule(String hoscode, String hosScheduleId);

    Page<Schedule> getScheduleListByConditionsInPages(Integer current, Integer size, ScheduleQueryVo scheduleQueryVo);

    Map<String, Object> showSchedulePage(Integer current, Integer size, String hoscode, String depcode);

    List<Schedule> getDetailSchedule(String hoscode, String depcode, String workDate);

    default String getDateOfWeek(DateTime dateTime) {
        String dayOfWeek = "";
        switch (dateTime.getDayOfWeek()) {
            case DateTimeConstants.SUNDAY:
                dayOfWeek = "周日";
                break;
            case DateTimeConstants.MONDAY:
                dayOfWeek = "周一";
                break;
            case DateTimeConstants.TUESDAY:
                dayOfWeek = "周二";
                break;
            case DateTimeConstants.WEDNESDAY:
                dayOfWeek = "周三";
                break;
            case DateTimeConstants.THURSDAY:
                dayOfWeek = "周四";
                break;
            case DateTimeConstants.FRIDAY:
                dayOfWeek = "周五";
                break;
            case DateTimeConstants.SATURDAY:
                dayOfWeek = "周六";
            default:
                break;
        }
        return dayOfWeek;
    }

    default Schedule packSchedule(Schedule schedule, HospitalService hospitalService, DepartmentService departmentService) {
        String hoscode = schedule.getHoscode();
        String depcode = schedule.getDepcode();
        Date workDate = schedule.getWorkDate();

        String hosname = hospitalService.getHospitalByHoscode(hoscode).getHosname();
        String depname = departmentService.getDepartmentByDepCodeAndHosCode(depcode, hoscode).getDepname();
        String dateOfWeek = getDateOfWeek(new DateTime(workDate));

        Map<String, Object> param = schedule.getParam();
        param.put("hosname", hosname);
        param.put("depname", depname);
        param.put("dayOfWeek", dateOfWeek);
        schedule.setParam(param);

        return schedule;
    }

}
