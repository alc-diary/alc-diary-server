package com.alc.diary.presentation.api;

import com.alc.diary.application.auth.dto.request.ReissueAccessTokenAppRequest;
import com.alc.diary.application.auth.dto.request.SocialLoginAppRequest;
import com.alc.diary.application.auth.dto.response.ReissueAccessTokenAppResponse;
import com.alc.diary.application.auth.dto.response.SocialLoginAppResponse;
import com.alc.diary.application.auth.service.RefreshTokenAppService;
import com.alc.diary.application.auth.service.SocialLoginAppService;
import com.alc.diary.domain.user.enums.SocialType;
import com.alc.diary.infrastructure.external.client.feign.kakao.dto.response.KakaoLoginResponse;
import com.alc.diary.presentation.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
@RestController
public class AuthApiController {

    private final SocialLoginAppService socialLoginAppService;
    private final RefreshTokenAppService refreshTokenAppService;

    @PostMapping("/social-login")
    public ApiResponse<SocialLoginAppResponse> socialLogin(
        @RequestHeader("Authorization") String bearerToken,
        @RequestParam SocialType socialType
    ) {
        if (bearerToken != null && !bearerToken.startsWith("Bearer ")) {
            throw new IllegalArgumentException("Invalid Authorization");
        }
        String socialAccessToken = bearerToken.substring("Bearer ".length());
        SocialLoginAppRequest request = new SocialLoginAppRequest(socialType, socialAccessToken);
        return ApiResponse.getSuccess(socialLoginAppService.login(request));
    }

    @PostMapping("/access-token/reissue")
    public ApiResponse<ReissueAccessTokenAppResponse> reissueAccessToken(
        @RequestBody ReissueAccessTokenAppRequest request
    ) {
        return ApiResponse.getSuccess(refreshTokenAppService.reissueToken(request));
    }

    @PostMapping("/kakao/success")
    public ApiResponse<SocialLoginAppResponse> kakaoLogin(
            @RequestBody KakaoLoginResponse request
    ) {
        return ApiResponse.getSuccess(socialLoginAppService.kakaoLogin(request));
    }
}
