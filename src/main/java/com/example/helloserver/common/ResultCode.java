package com.example.helloserver.common;

import lombok.Getter;

@Getter
public enum ResultCode {
    SUCCESS(200, "成功"),
    TOKEN_INVALID(401, "Token无效"),
    PARAM_ERROR(400, "参数错误"),
    SYSTEM_ERROR(500, "系统异常");

    private final Integer code;
    private final String message;

    ResultCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}