package com.alc.diary.application.auth.strategy;

import com.alc.diary.application.auth.strategy.dto.SocialLoginStrategyRequest;
import com.alc.diary.application.auth.strategy.dto.SocialLoginStrategyResponse;
import com.alc.diary.infrastructure.external.client.feign.google.GoogleFeignClient;
import com.alc.diary.infrastructure.external.client.feign.google.dto.response.GoogleUserInfoDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class GoogleLoginService implements SocialLoginStrategy {
    private final GoogleFeignClient feignClient;

    @Override
    public SocialLoginStrategyResponse login(SocialLoginStrategyRequest request) {
        String bearerToken = "Bearer " + request.socialAccessToken();
        ResponseEntity<GoogleUserInfoDto> userInfoResponse = feignClient.getUserInfo(bearerToken);
        if (userInfoResponse.getStatusCode().isError()) {
            throw new IllegalArgumentException("Google authentication error.");
        }
        System.out.println(userInfoResponse.getBody());

        return null;
    }
}
