package com.hilda.yygh.cmn.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@EnableFeignClients
@SpringBootConfiguration
@MapperScan(basePackages = "com.hilda.yygh.cmn.mapper")
@ComponentScan(basePackages = "com.hilda")
public class ServiceCmnConfig {

}
