package com.example.alcdiary.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

/**
 * 무중단 배포를 위해 생성한 controller
 */
@RequiredArgsConstructor
@RestController
public class ProfileController {

    private final Environment env;

    @GetMapping("/profile")
    public String profile() {
        List<String> profiles = Arrays.asList(env.getActiveProfiles());
        List<String> productProfiles = Arrays.asList("product-1", "product-2");

        return profiles.stream()
                .filter(productProfiles::contains)
                .findAny()
                .orElse("default");
    }
}
