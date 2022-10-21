package com.hilda.yygh.oss.controller;

import com.hilda.common.result.R;
import com.hilda.yygh.oss.controller.service.FileUploadService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@Api("阿里云文件上传接口")
@RestController
@RequestMapping("/admin/oss/file")
public class FileUploadController {

    //Postman:
    //https://shangyitong0526.oss-cn-hangzhou.aliyuncs.com/2022/10/21/29fbd7a1a0d8443c96a51b458de8df4f%E9%97%AE%E5%8F%B7%E9%B9%85%E5%A4%B4%E5%83%8F.jpg

    //aliyun:
    //https://shangyitong0526.oss-cn-hangzhou.aliyuncs.com/2022/10/21/29fbd7a1a0d8443c96a51b458de8df4f%E9%97%AE%E5%8F%B7%E9%B9%85%E5%A4%B4%E5%83%8F.jpg
    @Autowired
    private FileUploadService fileUploadService;

    @ApiOperation("上传文件")
    @PostMapping("/upload")
    public R upload(@RequestParam("file") MultipartFile file) {
        InputStream inputStream = null;
        try {
            inputStream = file.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
            return R.error().code(202).message("文件解析失败");
        }
        String url = fileUploadService.upload(file.getOriginalFilename(), inputStream);

        return R.ok().data("url", url).message("文件上传成功");
    }

}
