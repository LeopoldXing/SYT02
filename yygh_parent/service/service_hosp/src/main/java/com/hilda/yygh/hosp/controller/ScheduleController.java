package com.hilda.yygh.hosp.controller;

import com.hilda.common.result.R;
import com.hilda.yygh.hosp.service.ScheduleService;
import com.hilda.yygh.model.hosp.Schedule;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@Api("排班接口")
@RestController
@RequestMapping("/admin/hosp/schedule")
public class ScheduleController {

    @Autowired
    private ScheduleService scheduleService;

    @ApiOperation("根据医院编号 和 科室编号 ，查询排班规则数据")
    @GetMapping("/getScheduleRule/{current}/{size}/{hoscode}/{depcode}")
    public R getScheduleRuleByHoscodeAndDepcode(@PathVariable Integer current,
                                                @PathVariable Integer size,
                                                @PathVariable String hoscode,
                                                @PathVariable String depcode) {
        Map<String, Object> param = scheduleService.showSchedulePage(current, size, hoscode, depcode);

        return R.ok().data(param);
    }

    //根据医院编号 、科室编号和工作日期，查询排班详细信息
    @ApiOperation(value = "查询排班详细信息")
    @GetMapping("/getScheduleDetail/{hoscode}/{depcode}/{workDate}")
    public R getScheduleDetail( @PathVariable String hoscode,
                                @PathVariable String depcode,
                                @PathVariable String workDate) {
        List<Schedule> list = scheduleService.getDetailSchedule(hoscode,depcode,workDate);
        return R.ok().data("list",list);
    }

}
