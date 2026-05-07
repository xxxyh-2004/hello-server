package com.example.helloserver.config;

import com.example.helloserver.security.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(Customizer.withDefaults()) // 开启CORS
                .csrf(AbstractHttpConfigurer::disable) // 关闭CSRF
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 无状态
                )
                .authorizeHttpRequests(auth -> auth
                        // 放行注册
                        .requestMatchers(HttpMethod.POST, "/api/users").permitAll()
                        // 放行登录
                        .requestMatchers(HttpMethod.POST, "/api/users/login").permitAll()
                        // 放行聊天接口
                        .requestMatchers("/api/chat/**").permitAll()
                        // 其他全部需要认证
                        .anyRequest().authenticated()
                )
                // JWT 过滤器在用户名密码认证之前执行
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .formLogin(AbstractHttpConfigurer::disable) // 关闭默认表单登录
                .httpBasic(AbstractHttpConfigurer::disable); // 关闭httpBasic

        return http.build();
    }
}