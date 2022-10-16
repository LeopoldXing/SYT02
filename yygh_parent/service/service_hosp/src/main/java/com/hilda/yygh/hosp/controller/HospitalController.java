package com.hilda.yygh.hosp.controller;

import com.hilda.common.result.R;
import com.hilda.yygh.cmn.client.DictFeignClient;
import com.hilda.yygh.hosp.service.HospitalService;
import com.hilda.yygh.model.hosp.Hospital;
import com.hilda.yygh.vo.hosp.HospitalQueryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@Api("医院接口")
@RestController
@RequestMapping("/admin/hosp/hospital")
public class HospitalController {

    @Autowired
    private HospitalService hospitalService;

    @Autowired
    private DictFeignClient dictFeignClient;

    @ApiOperation("展示医院详情")
    @GetMapping("/show/{id}")
    public R showHospitalDetails(@PathVariable String id) {
        Hospital hospital = hospitalService.getHospitalById(id);

        if (hospital == null) return R.error().code(201).message("根据指定id未找到医院");

        hospitalService.packHospital(hospital);

        HashMap<String, Object> param = new HashMap<>();
        param.put("hospital", hospital);
        param.put("bookingRule", hospital.getBookingRule());
        return R.ok().data("hospital", param);
    }

    @ApiOperation("分页条件查询 医院")
    @GetMapping("/{current}/{size}")
    public R getHospitalListByConditionsInPages(@PathVariable Integer current, @PathVariable Integer size, HospitalQueryVo hospitalQueryVo) {
        Page<Hospital> hospitalByConditionsInPages = hospitalService.getHospitalByConditionsInPages(current, size, hospitalQueryVo);
        return R.ok().data("pages", hospitalByConditionsInPages);
    }

    @ApiOperation("更改医院状态")
    @GetMapping("/updateStatus/{id}/{status}")
    public R updateHospitalStatus(@PathVariable String id, @PathVariable Integer status) {
        return hospitalService.updateStatus(id, status) ? R.ok() : R.error().code(201).message("根据指定id未找到医院");
    }

}
