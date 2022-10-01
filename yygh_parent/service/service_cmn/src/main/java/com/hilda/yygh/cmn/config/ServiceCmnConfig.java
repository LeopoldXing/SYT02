package com.hilda.yygh.cmn.config;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

@SpringBootConfiguration
@MapperScan(basePackages = "com.hilda.yygh.cmn.mapper")
@ComponentScan(basePackages = "com.hilda")
public class ServiceCmnConfig {

    //分页拦截器
    @Bean
    public PaginationInterceptor paginationInterceptor(){
        return new PaginationInterceptor();
    }

}
