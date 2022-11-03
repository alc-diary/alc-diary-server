package com.example.alcdiary;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class AlcDiaryApplication {

	public static void main(String[] args) {
		SpringApplication.run(AlcDiaryApplication.class, args);
	}

}
