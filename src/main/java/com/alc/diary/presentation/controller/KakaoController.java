package com.alc.diary.presentation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@RequestMapping("/kakao")
@Controller
public class KakaoController {

    @GetMapping("/login")
    public String kakaoLogin() {
        return "kakao";
    }

    @GetMapping("/redirect")
    public String kakaoRedirect() {
        return "kakao-redirect";
    }
}
