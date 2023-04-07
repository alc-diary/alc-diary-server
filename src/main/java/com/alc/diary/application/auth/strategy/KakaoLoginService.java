package com.alc.diary.application.auth.strategy;

import com.alc.diary.application.auth.strategy.dto.SocialLoginStrategyRequest;
import com.alc.diary.application.auth.strategy.dto.SocialLoginStrategyResponse;
import com.alc.diary.infrastructure.external.client.feign.kakao.KakaoFeignClient;
import com.alc.diary.infrastructure.external.client.feign.kakao.dto.response.KakaoLoginResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class KakaoLoginService implements SocialLoginStrategy {

    private final KakaoFeignClient kakaoFeignClient;

    @Override
    public SocialLoginStrategyResponse login(SocialLoginStrategyRequest request) {
        String bearerToken = "Bearer " + request.socialAccessToken();
        ResponseEntity<KakaoLoginResponse> kakaoLoginResponseResponseEntity = kakaoFeignClient.kakaoLogin(bearerToken);
        return new SocialLoginStrategyResponse(
                String.valueOf(kakaoLoginResponseResponseEntity.getBody().getId())
        );
    }
}
