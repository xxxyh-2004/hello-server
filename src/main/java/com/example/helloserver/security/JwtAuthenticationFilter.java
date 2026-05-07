package com.example.helloserver.security;

import com.example.helloserver.entity.User;
import com.example.helloserver.mapper.UserMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserMapper userMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        // 1. 获取请求头中的 Authorization
        String authHeader = request.getHeader("Authorization");

        // 2. 如果没有 Authorization 或不是 Bearer 开头，直接放行
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 3. 提取 JWT 字符串（去掉 "Bearer " 前缀）
        String jwt = authHeader.substring(7);
        String username;

        try {
            // 4. 从 JWT 中解析用户名
            username = jwtUtil.extractUsername(jwt);
        } catch (Exception e) {
            // token 解析失败，直接放行（后续会被 SecurityConfig 拦截）
            filterChain.doFilter(request, response);
            return;
        }

        // 5. 如果用户名有效且当前未认证，设置认证信息
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            User user = userMapper.selectOne(
                    new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<User>()
                            .eq(User::getUsername, username)
            );
            if (user != null && jwtUtil.validateToken(jwt, username)) {
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(user, null, Collections.emptyList());
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        filterChain.doFilter(request, response);
    }
}
