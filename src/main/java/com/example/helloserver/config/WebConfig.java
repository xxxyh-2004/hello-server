package com.example.helloserver.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    // 这里什么都不用写！
    // 拦截器全部删掉，交给 Spring Security 管理

}