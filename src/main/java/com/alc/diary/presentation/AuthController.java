package com.alc.diary.presentation;

import com.alc.diary.application.auth.dto.request.SocialLoginAppRequest;
import com.alc.diary.application.auth.dto.response.SocialLoginAppResponse;
import com.alc.diary.application.auth.service.SocialLoginAppService;
import com.alc.diary.domain.user.enums.SocialType;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
@RestController
public class AuthController {

    private final SocialLoginAppService socialLoginAppService;

    @PostMapping("/social-login")
    public SocialLoginAppResponse socialLogin(
            @RequestHeader("Authorization") String bearerToken,
            @RequestParam SocialType socialType
    ) {
        if (bearerToken != null && !bearerToken.startsWith("Bearer ")) {
            throw new IllegalArgumentException("Invalid Authorization");
        }
        String socialAccessToken = bearerToken.substring("Bearer ".length());
        SocialLoginAppRequest request = new SocialLoginAppRequest(socialType, socialAccessToken);
        return socialLoginAppService.login(request);
    }
}
