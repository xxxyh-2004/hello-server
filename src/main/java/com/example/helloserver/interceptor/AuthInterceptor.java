package com.example.helloserver.interceptor;

import com.example.helloserver.common.Result;
import com.example.helloserver.common.ResultCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;

public class AuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("Authorization");
        if (token == null || !token.startsWith("Bearer ")) {
            response.setContentType("application/json;charset=utf-8");
            Result result = Result.error(ResultCode.TOKEN_INVALID);
            new ObjectMapper().writeValue(response.getWriter(), result);
            return false;
        }
        return true;
    }
}