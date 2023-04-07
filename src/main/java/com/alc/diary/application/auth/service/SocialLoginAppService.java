package com.alc.diary.application.auth.service;

import com.alc.diary.application.auth.dto.request.SocialLoginAppRequest;
import com.alc.diary.application.auth.strategy.dto.SocialLoginStrategyRequest;
import com.alc.diary.application.auth.dto.response.SocialLoginAppResponse;
import com.alc.diary.application.auth.strategy.dto.SocialLoginStrategyResponse;
import com.alc.diary.application.auth.factory.SocialLoginStrategyFactory;
import com.alc.diary.application.auth.strategy.SocialLoginStrategy;
import com.alc.diary.domain.user.User;
import com.alc.diary.domain.user.enums.DescriptionStyle;
import com.alc.diary.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class SocialLoginAppService {

    private final SocialLoginStrategyFactory socialLoginStrategyFactory;
    private final UserRepository userRepository;
    private final JwtService jwtService;

    @Transactional
    public SocialLoginAppResponse login(SocialLoginAppRequest request) {
        SocialLoginStrategy socialLoginStrategy =
                socialLoginStrategyFactory.getSocialLoginStrategy(request.socialType());
        SocialLoginStrategyResponse socialLoginStrategyResponse =
                socialLoginStrategy.login(new SocialLoginStrategyRequest(request.socialAccessToken()));
        User findUser = userRepository.findBySocialTypeAndSocialId(request.socialType(), socialLoginStrategyResponse.socialUserId())
                .orElseGet(() -> {
                    User user = User.builder(
                                    request.socialType(),
                                    socialLoginStrategyResponse.socialUserId(),
                                    DescriptionStyle.MILD)
                            .build();
                    User savedUser = userRepository.save(user);
                    return savedUser;
                });

        String accessToken = jwtService.generateToken(findUser.getId());
        String refreshToken = UUID.randomUUID().toString();
        return new SocialLoginAppResponse(accessToken, refreshToken);
    }
}
