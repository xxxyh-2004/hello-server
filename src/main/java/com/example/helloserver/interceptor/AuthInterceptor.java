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
        // 获取请求方法：GET POST PUT DELETE
        String method = request.getMethod();

        // ===================== 附加题核心逻辑 =====================
        // 如果是 GET 或 POST → 直接放行，不需要 token
        if ("GET".equals(method) || "POST".equals(method)) {
            return true;
        }

        // 如果是 PUT / DELETE → 必须校验 token
        String token = request.getHeader("Authorization");
        if (token == null || token.trim().isEmpty()) {
            response.setContentType("application/json;charset=utf-8");
            Result result = Result.error(ResultCode.TOKEN_INVALID);
            new ObjectMapper().writeValue(response.getWriter(), result);
            return false;
        }

        // 有 token → 放行
        return true;
    }
}