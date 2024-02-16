package com.alc.diary.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.RequestPredicates;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.RequestParameterBuilder;
import springfox.documentation.service.ParameterType;
import springfox.documentation.service.RequestParameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

@Configuration
public class SwaggerConfig {

    private RequestParameter createAuthorizationHeader() {
        return new RequestParameterBuilder()
                .name("Authorization")
                .description("Access Token")
                .in(ParameterType.HEADER)
                .required(true)
                .query(q -> q.model(m -> m.name("Authorization")))
                .build();
    }

    @Bean
    public Docket apiForTest() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("Test APIs")
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.alc.diary.presentation"))
                .paths(PathSelectors.ant("/test/**"))
                .build();
    }

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("General APIs")
                .globalRequestParameters(List.of(createAuthorizationHeader()))
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.alc.diary.presentation"))
                .paths(Predicate.not(PathSelectors.ant("/test/**")))
                .build();
    }
}
