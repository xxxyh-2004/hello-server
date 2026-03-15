package com.example.helloserver.exception;

import com.example.helloserver.common.Result;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 捕获所有异常
    @ExceptionHandler(Exception.class)
    public Result<String> handleException(Exception e) {
        return Result.error("服务器异常：" + e.getMessage());
    }
}
