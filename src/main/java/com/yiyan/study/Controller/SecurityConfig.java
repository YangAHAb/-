package com.yiyan.study.Controller;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 允许跨域请求
                .cors(cors -> cors.disable()) // 如果需要禁用 CORS 保护，可以使用 cors.disable()，否则请设置合适的 CORS 配置
                // 禁用 CSRF 保护
                .csrf(csrf -> csrf.disable()) 
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/api/**").permitAll() // 允许访问 /api/** 路径
                        .anyRequest().authenticated() // 其他请求需要身份验证
                );

        return http.build();
    }
}
