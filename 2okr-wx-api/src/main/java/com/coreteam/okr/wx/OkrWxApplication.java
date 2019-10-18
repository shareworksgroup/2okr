package com.coreteam.okr.wx;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @ClassName: OkrWxApplication
 * @Description TODO
 * @Author sean.deng
 * @Date 2019/07/18 11:01
 * @Version 1.0.0
 */
@SpringBootApplication
@EnableTransactionManagement
@EnableFeignClients(basePackages = {"com.coreteam.okr.client"})
@ComponentScan(basePackages = {"com.coreteam.web","com.coreteam.okr","com.coreteam.okr.wx","com.coreteam.resourceserver"})
@MapperScan(basePackages="com.coreteam.okr.dao")
public class OkrWxApplication {
    public static void main(String[] args) {
        SpringApplication.run(OkrWxApplication.class, args);
    }
}
