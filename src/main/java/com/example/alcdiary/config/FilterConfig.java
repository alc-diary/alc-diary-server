package com.example.alcdiary.config;

import com.example.alcdiary.application.util.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
public class FilterConfig {

    private final JwtProvider jwtProvider;

//    @Bean
//    FilterRegistrationBean<ExceptionHandlerFilter> exceptionHandlerFilter() {
//        FilterRegistrationBean<ExceptionHandlerFilter> filterRegistrationBean = new FilterRegistrationBean<>();
//        filterRegistrationBean.setFilter(new ExceptionHandlerFilter());
//        filterRegistrationBean.setOrder(Integer.MIN_VALUE);
//        return filterRegistrationBean;
//    }
//
//    @Bean
//    FilterRegistrationBean<RequestFilter> requestFilter() {
//        FilterRegistrationBean<RequestFilter> filterRegistrationBean = new FilterRegistrationBean<>();
//        filterRegistrationBean.setFilter(new RequestFilter(jwtProvider));
//        filterRegistrationBean.setOrder(0);
//        return filterRegistrationBean;
//    }
}
