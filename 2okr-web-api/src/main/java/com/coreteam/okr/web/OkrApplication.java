package com.coreteam.okr.web;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
@EnableFeignClients(basePackages = {"com.coreteam.okr.client"})
@EnableScheduling
@ComponentScan(basePackages = {"com.coreteam.web","com.coreteam.okr","com.coreteam.okr.web","com.coreteam.resourceserver"})
@MapperScan(basePackages="com.coreteam.okr.dao")
public class OkrApplication {

    public static void main(String[] args) {
        SpringApplication.run(OkrApplication.class, args);
    }

}
