package com.example.WEEK04.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // 모든 엔드포인트 허용
                .allowedOrigins("http://localhost:5174", "http://localhost:5173", "http://localhost:4173", "http://localhost:4174","http://172.20.4.236:5173") // 프론트엔드 주소
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS","PATCH")
                .allowedHeaders("*") // 모든 헤더 허용
                .allowCredentials(true) // 쿠키/세션 포함 허용
                .maxAge(3600); // preflight 캐시 (1시간)
    }
}
