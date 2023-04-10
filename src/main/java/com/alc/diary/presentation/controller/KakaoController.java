package com.alc.diary.presentation.controller;

import com.alc.diary.application.auth.dto.response.SocialLoginAppResponse;
import com.alc.diary.application.auth.service.SocialLoginAppService;
import com.alc.diary.infrastructure.external.client.feign.kakao.dto.response.KakaoLoginResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@RequiredArgsConstructor
@RequestMapping("/kakao")
@Controller
public class KakaoController {

    private final SocialLoginAppService socialLoginAppService;

    @GetMapping("/login")
    public String kakaoLogin() {
        return "kakao";
    }

    @GetMapping("/login/success")
    public String kakaoLoginSuccess(
            @RequestBody KakaoLoginResponse request,
            RedirectAttributes redirectAttributes
    ) {
        SocialLoginAppResponse response = socialLoginAppService.kakaoLogin(request);
        redirectAttributes.addFlashAttribute("response", response);

        return "redirect:/kakao/redirect";
    }

    @GetMapping("/redirect")
    public String kakaoRedirect() {
        return "kakao-redirect";
    }
}
