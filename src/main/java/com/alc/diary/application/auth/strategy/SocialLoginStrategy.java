package com.alc.diary.application.auth.strategy;

import com.alc.diary.application.auth.strategy.dto.SocialLoginStrategyRequest;
import com.alc.diary.application.auth.strategy.dto.SocialLoginStrategyResponse;

public interface SocialLoginStrategy {
    SocialLoginStrategyResponse login(SocialLoginStrategyRequest request);
}
