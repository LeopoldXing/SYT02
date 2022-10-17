package com.hilda.yygh.hosp.repository;

import com.hilda.yygh.model.hosp.Schedule;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface ScheduleRepository extends MongoRepository<Schedule, String> {

    /**
     * 根据 医院编号 和 排班编号 查询 排班
     * @param hoscode 医院编号
     * @param hosScheduleId 排版编号
     * @return
     */
    Schedule getScheduleByHoscodeAndHosScheduleId (String hoscode, String hosScheduleId);

    List<Schedule> getSchedulesByHoscodeAndDepcodeAndWorkDate (String hoscode, String depcode, Date workDate);
}
