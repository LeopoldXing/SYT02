package com.hilda.yygh.hosp.controller.api;

import com.hilda.common.result.R;
import com.hilda.yygh.hosp.service.DepartmentService;
import com.hilda.yygh.hosp.service.HospitalService;
import com.hilda.yygh.model.hosp.Hospital;
import com.hilda.yygh.vo.hosp.HospitalQueryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api("用户使用的医院接口")
@RestController
@RequestMapping("/api/hosp/hospital")
public class HospitalApiController {

    @Autowired
    private HospitalService hospitalService;

    @Autowired
    private DepartmentService departmentService;

    @GetMapping("/{current}/{size}")
    @ApiOperation("分页条件查询医院")
    public R getHospitalListByConditionsInPages(@PathVariable Integer current, @PathVariable Integer size, HospitalQueryVo hospitalQueryVo) {
        Page<Hospital> hospitalPage = hospitalService.getHospitalByConditionsInPages(current, size, hospitalQueryVo);

        return R.ok().data("pages", hospitalPage);
    }

    @ApiOperation("根据给定的医院名称进行模糊查询")
    @GetMapping("/findByHosname/{hosname}")
    public R getHospitalByHosname(@PathVariable String hosname) {
        List<Hospital> hospitalList = hospitalService.getHospitalByHosname(hosname);
        return R.ok().data("list", hospitalList);
    }

    @ApiOperation("根据医院编号查询部门列表")
    @GetMapping("/department/{hoscode}")
    public R getDepartmentVoListByHoscode (@PathVariable String hoscode) {
        return R.ok().data("list", departmentService.getDepartmentListByHoscode(hoscode));
    }

    @ApiOperation(value = "医院预约挂号详情")
    @GetMapping("/{hoscode}")
    public R getHospitalInfoByHoscode(@PathVariable String hoscode) {
        Hospital hospitalByHoscode = hospitalService.getHospitalByHoscode(hoscode);
        Map<String, Object> res = new HashMap<>();
        res.put("hospital", hospitalByHoscode);
        res.put("bookingRule", hospitalByHoscode.getBookingRule());

        return R.ok().data(res);
    }

}
