package com.example.WEEK04.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.responses.ApiResponse;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "WEEK04 REST API 문서",
                version = "1.0",
                description = "Spring Boot 3 + Swagger + ExceptionHandler 연동 예시"
        )
)
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(new Components()
                        // 전역 스키마 등록 (공통 응답 포맷)
                        .addSchemas("ApiResponse", new Schema<>()
                                .description("공통 API 응답 포맷 (code, message, data 구조)"))
                        .addSchemas("ErrorResponse", new Schema<>()
                                .description("에러 응답 포맷 (code, message, detail 구조)"))

                        // 전역 에러 응답 정의
                        .addResponses("InternalServerError",
                                new ApiResponse()
                                        .description("서버 내부 오류 (500)")
                                        .content(null))
                )
                .info(new io.swagger.v3.oas.models.info.Info()
                        .title("WEEK04 REST API")
                        .description("OpenAPI 3 기반 자동 문서화 설정")
                        .version("v1.0")
                        .contact(new Contact()
                                .name("juncci")
                                .email("pjseo1313@daum.net")));
    }

    @Bean
    public GroupedOpenApi defaultApi() {
        return GroupedOpenApi.builder()
                .group("default")
                .pathsToMatch("/**")
                .build();
    }
}
