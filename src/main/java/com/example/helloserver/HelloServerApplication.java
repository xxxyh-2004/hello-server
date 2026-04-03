package com.example.helloserver;

import com.baomidou.mybatisplus.autoconfigure.MybatisPlusAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// 只加了这一段：exclude = MybatisPlusAutoConfiguration.class
@SpringBootApplication(exclude = MybatisPlusAutoConfiguration.class)
public class HelloServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(HelloServerApplication.class, args);
    }

}