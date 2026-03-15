package com.example.helloserver;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

// 标记为REST控制器，返回JSON/字符串而非页面
@RestController
public class HelloController {
    // 定义GET请求接口，路径为/hello
    @GetMapping("/hello")
    public String sayHello() {
        // 接口返回的内容，可自定义
        return "Hello Spring Boot 3.x! This is my first server API.";
    }
}
