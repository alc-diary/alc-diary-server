package com.alc.diary.application.auth.factory;

import com.alc.diary.application.auth.strategy.AppleLoginService;
import com.alc.diary.application.auth.strategy.GoogleLoginService;
import com.alc.diary.application.auth.strategy.KakaoLoginService;
import com.alc.diary.application.auth.strategy.SocialLoginStrategy;
import com.alc.diary.domain.user.enums.SocialType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class SocialLoginStrategyFactory {

    private final KakaoLoginService kakaoLoginService;
    private final GoogleLoginService googleLoginService;
    private final AppleLoginService appleLoginService;

    public SocialLoginStrategy getSocialLoginStrategy(SocialType socialType) {
        switch (socialType) {
            case KAKAO -> {
                return kakaoLoginService;
            }
            case GOOGLE -> {
                return googleLoginService;
            }
            case APPLE -> {
                return appleLoginService;
            }
            default -> {
                throw new IllegalArgumentException("잘못된 SocialType 입니다.");
            }
        }
    }
}
