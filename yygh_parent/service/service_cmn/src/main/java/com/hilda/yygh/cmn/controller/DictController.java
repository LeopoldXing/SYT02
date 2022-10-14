package com.hilda.yygh.cmn.controller;

import com.hilda.common.result.R;
import com.hilda.yygh.cmn.service.DictService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

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
    public void exportDictData(HttpServletResponse response) {
        String fileName = handleResponseForDownloadingExcel(response);

        try {
            dictService.exportDictData(response.getOutputStream(), fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @ApiOperation("导入数据字典")
    @PostMapping("/importDictData")
    public R importDictData(MultipartFile file) {
        try {
            dictService.importDictData(file.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return R.ok();
    }

    @ApiOperation("根据value值和父节点编号查询字典名称")
    @GetMapping(value = "/getName/{parentDictCode}/{value}")
    public String getNameByParentDictCodeAndValue(@PathVariable("parentDictCode") String parentDictCode, @PathVariable("value") Long value) {
        return dictService.getNameByParentDictCodeAndValue(parentDictCode, value);
    }

    @ApiOperation("根据value值获取字典内容")
    @GetMapping(value = "/getName/{value}")
    public String getNameByValue (@PathVariable("value") Long value) {
        return dictService.getNameByValue(value);
    }

    //处理导出数据字典时的响应
    private String handleResponseForDownloadingExcel(HttpServletResponse response) {
        //指定响应内容的格式
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");

        //URLEncoder.encode可以防止中文乱码 和easyexcel没有关系
        String fileName = null;
        try {
            fileName = URLEncoder.encode("数据字典", "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        //指示响应的内容以附件的形式下载
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");

        return fileName;
    }

}
