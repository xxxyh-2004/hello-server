package com.example.helloserver.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

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
                        // 其他全部需要认证
                        .anyRequest().authenticated()
                )
                .formLogin(AbstractHttpConfigurer::disable) // 关闭默认表单登录
                .httpBasic(AbstractHttpConfigurer::disable); // 关闭httpBasic

        return http.build();
    }
}