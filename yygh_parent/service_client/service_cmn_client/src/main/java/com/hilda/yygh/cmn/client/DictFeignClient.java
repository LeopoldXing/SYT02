package com.hilda.yygh.cmn.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("service-cmn") // value值应为被调用服务 在注册中心的 应用名称
public interface DictFeignClient {

    @GetMapping(value = "/admin/cmn/dict/getName/{parentDictCode}/{value}")
    String getNameByParentDictCodeAndValue(@PathVariable("parentDictCode") String parentDictCode, @PathVariable("value") Long value);

    @GetMapping(value = "/admin/cmn/dict/getName/{value}")
    String getNameByValue (@PathVariable("value") Long value);

}
