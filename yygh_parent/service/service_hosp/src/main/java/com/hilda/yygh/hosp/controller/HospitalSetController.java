package com.hilda.yygh.hosp.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hilda.common.result.R;
import com.hilda.yygh.hosp.service.HospitalSetService;
import com.hilda.yygh.model.hosp.HospitalSet;
import com.hilda.yygh.vo.hosp.HospitalSetQueryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api("医院设置接口")
@RestController
@RequestMapping("/admin/hosp/hospitalSet")
@CrossOrigin
public class HospitalSetController {

    @Autowired
    private HospitalSetService hospitalSetService;

    @ApiOperation("查询所有 医院设置")
    @GetMapping("/getAll")
    public R getAllHospitalSet() {
        List<HospitalSet> hospitalSetList = hospitalSetService.getAllHospitalSets();
        if (hospitalSetList == null) {
            return R.error();
        } else {
            return R.ok().data("list", hospitalSetList);
        }
    }

    @ApiOperation("分页查询 全部 医院设置")
    @GetMapping("/{pageNum}/{limit}")
    public R getAllInPages(@PathVariable("pageNum") Long pageNum, @PathVariable("limit") Long limit) {
        Page<HospitalSet> page = new Page<>(pageNum, limit);
        hospitalSetService.page(page, null);

        List<HospitalSet> rows = page.getRecords();
        Long total = page.getTotal();

        return R.ok().data("total", total).data("rows", rows);
    }

    @ApiOperation("分页 条件查询 医院设置")
    @PostMapping("/{current}/{size}")
    public R getByConditionsInPages(@PathVariable("current") Long current, @PathVariable("size") Long size, @RequestBody HospitalSetQueryVo hospitalSetQueryVo) {
        Page<HospitalSet> page = new Page<>(current, size);
        QueryWrapper<HospitalSet> queryWrapper = new QueryWrapper<>();

        if (hospitalSetQueryVo != null) {
            //获取 医院名称 和 医院代号
            String hosname = hospitalSetQueryVo.getHosname();
            String hoscode = hospitalSetQueryVo.getHoscode();

            //对 医院名称 和 医院代号 进行判空后查询
            if (hosname != null) {
                queryWrapper.like("hosname", hosname);
            }
            if (hoscode != null) {
                queryWrapper.like("hoscode", hoscode);
            }
        }

        hospitalSetService.page(page, queryWrapper);

        return R.ok().data("rows", page.getRecords()).data("total", page.getTotal());
    }

    @ApiOperation("根据id 查询 医院设置")
    @GetMapping("/{id}")
    public R getHospitalSetById(@PathVariable Long id) {
        HospitalSet hospitalSet = hospitalSetService.getHospitalSetById(id);
        if (hospitalSet == null) {
            return R.error();
        } else {
            return R.ok().data("HospitalSet", hospitalSet);
        }
    }

    @ApiOperation("增加 医院设置")
    @PostMapping("/add")
    public R addHospitalSet(@RequestBody HospitalSet hospitalSet) {
        return hospitalSetService.addHospitalSet(hospitalSet) ? R.ok() : R.error();
    }

    @ApiOperation("根据id 删除 医院设置")
    @DeleteMapping("/{id}")
    public R deleteById(@ApiParam(name = "id", value = "医院设置编号", required = true) @PathVariable("id")
                                    Long id) {
        return hospitalSetService.deleteHospitalSetById(id) ? R.ok() : R.error();
    }

    @ApiOperation("根据id 批量删除 医院设置")
    @DeleteMapping("/deleteBatch")
    public R deleteBathByIdList(@RequestBody List<Long> idList) {
        return hospitalSetService.deleteHospitalSetByIdList(idList) ? R.ok() : R.error();
    }

    @ApiOperation("根据id 修改 医院设置")
    @PostMapping("/edit")
    public R editHospitalSetById(@RequestBody HospitalSet hospitalSet) {
        Boolean res = hospitalSetService.editHospitalSet(hospitalSet);

        return res ? R.ok() : R.error();
    }

    @ApiOperation("根据id 锁定/解锁 医院设置")
    @PutMapping("/lock/{id}/{status}")
    public R lockHospitalSet(@PathVariable("id") Long id, @PathVariable("status") Integer status) {
        Boolean res = hospitalSetService.lockHospitalSet(id, status);

        return res ? R.ok() : R.error();
    }

}
