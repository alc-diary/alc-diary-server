package com.alc.diary.presentation.api;

import com.alc.diary.application.auth.dto.request.ReissueAccessTokenAppRequest;
import com.alc.diary.application.auth.dto.request.SocialLoginAppRequest;
import com.alc.diary.application.auth.dto.response.ReissueAccessTokenAppResponse;
import com.alc.diary.application.auth.dto.response.SocialLoginAppResponse;
import com.alc.diary.application.auth.service.RefreshTokenAppService;
import com.alc.diary.application.auth.service.SocialLoginAppService;
import com.alc.diary.application.auth.strategy.KakaoLoginService;
import com.alc.diary.application.auth.strategy.dto.SocialLoginStrategyResponse;
import com.alc.diary.domain.user.enums.AgeRangeType;
import com.alc.diary.domain.user.enums.GenderType;
import com.alc.diary.domain.user.enums.SocialType;
import com.alc.diary.infrastructure.external.client.feign.google.dto.response.GoogleUserInfoDto;
import com.alc.diary.infrastructure.external.client.feign.kakao.dto.response.KakaoLoginResponse;
import com.alc.diary.presentation.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

import static com.alc.diary.domain.user.enums.AgeRangeType.*;
import static com.alc.diary.domain.user.enums.AgeRangeType.OVER_NINETY;

@RequiredArgsConstructor
@RequestMapping("/v1/auth")
@RestController
public class AuthApiController {

    private final SocialLoginAppService socialLoginAppService;
    private final RefreshTokenAppService refreshTokenAppService;

    @PostMapping("/social-login")
    public ApiResponse<SocialLoginAppResponse> socialLogin(
        @RequestHeader("Authorization") String bearerToken,
        @RequestHeader("User-Agent") String userAgent,
        @RequestParam SocialType socialType
    ) {
        if (bearerToken == null || !bearerToken.startsWith("Bearer ")) {
            throw new IllegalArgumentException("Invalid Authorization");
        }
        String socialAccessToken = bearerToken.substring("Bearer ".length());
        SocialLoginAppRequest request = new SocialLoginAppRequest(socialType, socialAccessToken);
        return ApiResponse.getSuccess(socialLoginAppService.login(request, userAgent));
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
        Optional<KakaoLoginResponse.KakaoAccount> kakaoAccountOptional = Optional.of(request.getKakaoAccount());
        Optional<KakaoLoginResponse.KakaoAccount.Profile> profileOptional = kakaoAccountOptional.map(KakaoLoginResponse.KakaoAccount::getProfile);

        String profileImageUrl = profileOptional.map(KakaoLoginResponse.KakaoAccount.Profile::getProfileImageUrl).orElse(null);
        String email = kakaoAccountOptional.map(KakaoLoginResponse.KakaoAccount::getEmail).orElse(null);
        GenderType gender = kakaoAccountOptional
            .map(KakaoLoginResponse.KakaoAccount::getGender)
            .map(s -> switch (s) {
                case "male" -> GenderType.MALE;
                case "female" -> GenderType.FEMALE;
                default -> throw new IllegalArgumentException("Invalid gender: " + s);
            })
            .orElse(null);
        AgeRangeType ageRange = kakaoAccountOptional.map(KakaoLoginResponse.KakaoAccount::getAgeRange)
            .map(s -> switch (s) {
                case "1~9" -> UNDER_TEN;
                case "15_19" -> TEN_TO_FOURTEEN;
                case "20~29" -> TWENTIES;
                case "30~39" -> THIRTIES;
                case "40~49" -> FORTIES;
                case "50~59" -> FIFTIES;
                case "60~69" -> SIXTIES;
                case "70~79" -> SEVENTIES;
                case "80~89" -> EIGHTIES;
                case "90~" -> OVER_NINETY;
                default -> throw new IllegalArgumentException("Invalid ageRange: " + s);
            })
            .orElse(null);

        SocialLoginStrategyResponse socialLoginStrategyResponse = new SocialLoginStrategyResponse(
            SocialType.KAKAO,
            String.valueOf(request.getId()),
            profileImageUrl,
            email,
            gender,
            ageRange
        );
        return ApiResponse.getSuccess(socialLoginAppService.webSocialLogin(socialLoginStrategyResponse));
    }

    @PostMapping("/google/success")
    public ApiResponse<SocialLoginAppResponse> googleLogin(
        @RequestBody GoogleUserInfoDto request
    ) {
        SocialLoginStrategyResponse socialLoginStrategyResponse = new SocialLoginStrategyResponse(
            SocialType.GOOGLE,
            request.id(),
            request.picture(),
            request.email(),
            null,
            null
        );
        return ApiResponse.getSuccess(socialLoginAppService.webSocialLogin(socialLoginStrategyResponse));
    }
}
