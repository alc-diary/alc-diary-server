package com.alc.diary;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
public class AlcDiaryApplication {

    public static void main(String[] args) {
        SpringApplication.run(AlcDiaryApplication.class, args);
    }
}

