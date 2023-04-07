package com.alc.diary.infrastructure.external.client.feign.kakao;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KakaoFeignConfig {

    @Bean
    public KakaoFeignInterceptor feignInterceptor() {
        return KakaoFeignInterceptor.of();
    }
}
