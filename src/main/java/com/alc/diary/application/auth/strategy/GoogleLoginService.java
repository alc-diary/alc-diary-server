package com.alc.diary.application.auth.strategy;

import com.alc.diary.application.auth.strategy.dto.SocialLoginStrategyRequest;
import com.alc.diary.application.auth.strategy.dto.SocialLoginStrategyResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class GoogleLoginService implements SocialLoginStrategy {

    @Override
    public SocialLoginStrategyResponse login(SocialLoginStrategyRequest request) {
        return null;
    }
}
