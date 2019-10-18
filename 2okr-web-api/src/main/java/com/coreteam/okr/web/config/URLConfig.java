package com.coreteam.okr.web.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * @ClassName: URLConfig
 * @Description TODO
 * @Author sean.deng
 * @Date 2019/06/12 12:54
 * @Version 1.0.0
 */
@Configuration
public class URLConfig {
    public static String  OKR_SERVICE_PATH;

    @Value("${okr-service.path}")
    private String okrServicePath;

    @PostConstruct
    public void init(){
        OKR_SERVICE_PATH=this.okrServicePath;
    }

}
