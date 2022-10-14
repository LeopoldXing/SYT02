package com.hilda.yygh.hosp.controller;

import com.hilda.common.result.R;
import com.hilda.yygh.hosp.service.HospitalService;
import com.hilda.yygh.model.hosp.Hospital;
import com.hilda.yygh.vo.hosp.HospitalQueryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@Api("医院接口")
@CrossOrigin
@RestController
@RequestMapping("/admin/hosp/hospital")
public class HospitalController {

    @Autowired
    private HospitalService hospitalService;

    @ApiOperation("分页条件查询 医院")
    @GetMapping("/{current}/{size}")
    public R getHospitalListByConditionsInPages(@PathVariable Integer current, @PathVariable Integer size, HospitalQueryVo hospitalQueryVo) {
        Page<Hospital> hospitalByConditionsInPages = hospitalService.getHospitalByConditionsInPages(current, size, hospitalQueryVo);
        return R.ok().data("pages", hospitalByConditionsInPages);
    }

}
