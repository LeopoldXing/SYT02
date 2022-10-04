package com.hilda.yygh.cmn.controller;

import com.hilda.common.result.R;
import com.hilda.yygh.cmn.service.DictService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api("数据字典接口")
@RestController
@CrossOrigin
@RequestMapping("/admin/cmn/dict")
public class DictController {

    @Autowired
    private DictService dictService;

    @ApiOperation("根据给定id 获取 子节点列表")
    @GetMapping("/getChildList/{id}")
    public R getChildList(@PathVariable("id") Long id) {
        return R.ok().data("list", dictService.getChildList(id));
    }

    @ApiOperation("导出数据字典")
    @GetMapping("/exportDictData")
    public R exportDictData() {

        return R.ok();
    }

}
