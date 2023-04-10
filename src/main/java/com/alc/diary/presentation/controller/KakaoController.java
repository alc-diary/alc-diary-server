package com.alc.diary.presentation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/kakao")
@Controller
public class KakaoController {

    @GetMapping("/login")
    public String kakaoLogin() {
        return "kakao";
    }

    @GetMapping("/redirect")
    public String kakaoRedirect(
            @RequestParam("accessToken") String accessToken,
            @RequestParam("refreshToken") String refreshToken,
            Model model
    ) {
        model.addAttribute("accessToken", accessToken);
        model.addAttribute("refreshToken", refreshToken);
        return "kakao-redirect";
    }
}
