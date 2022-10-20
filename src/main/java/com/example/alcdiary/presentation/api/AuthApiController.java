package com.example.alcdiary.presentation.api;

import com.example.alcdiary.domain.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/auth")
@RestController
public class AuthApiController {

    private final AuthService authService;

    @GetMapping
    public String test(
            @RequestParam String code
    ) {
        log.info(code);
        return code;
    }

    @GetMapping("/kakao/login")
    public void kakaoLogin(
            @RequestHeader("Authorization") String kakaoToken
    ) {
        log.info(kakaoToken);
        authService.kakaoLogin(kakaoToken);
    }

    @GetMapping("/login")
    public void login() {
        authService.login();
    }
}
