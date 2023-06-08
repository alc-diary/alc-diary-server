package com.alc.diary.application.auth.service;

import com.alc.diary.application.auth.dto.request.SocialLoginAppRequest;
import com.alc.diary.application.auth.strategy.dto.SocialLoginStrategyRequest;
import com.alc.diary.application.auth.dto.response.SocialLoginAppResponse;
import com.alc.diary.application.auth.strategy.dto.SocialLoginStrategyResponse;
import com.alc.diary.application.auth.factory.SocialLoginStrategyFactory;
import com.alc.diary.application.auth.strategy.SocialLoginStrategy;
import com.alc.diary.application.message.MessageService;
import com.alc.diary.domain.auth.RefreshToken;
import com.alc.diary.domain.auth.policy.DefaultExpiredPolicy;
import com.alc.diary.domain.auth.repository.RefreshTokenRepository;
import com.alc.diary.domain.user.User;
import com.alc.diary.domain.user.UserHistory;
import com.alc.diary.domain.user.enums.AgeRangeType;
import com.alc.diary.domain.user.enums.DescriptionStyle;
import com.alc.diary.domain.user.enums.GenderType;
import com.alc.diary.domain.user.enums.SocialType;
import com.alc.diary.domain.user.repository.UserHistoryRepository;
import com.alc.diary.domain.user.repository.UserRepository;
import com.alc.diary.infrastructure.external.client.feign.google.dto.response.GoogleUserInfoDto;
import com.alc.diary.infrastructure.external.client.feign.kakao.dto.response.KakaoLoginResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;
import java.util.UUID;

import static com.alc.diary.domain.user.enums.AgeRangeType.*;
import static com.alc.diary.domain.user.enums.AgeRangeType.OVER_NINETY;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class SocialLoginAppService {

    private final SocialLoginStrategyFactory socialLoginStrategyFactory;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final UserHistoryRepository userHistoryRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final MessageService messageService;

    @Transactional
    public SocialLoginAppResponse login(SocialLoginAppRequest request) {
        SocialLoginStrategyResponse socialLoginStrategyResponse = socialLogin(request);
        User foundUser = getUser(socialLoginStrategyResponse);

        String accessToken = jwtService.generateToken(foundUser.getId());
        RefreshToken refreshToken = RefreshToken.getDefault(foundUser);
        RefreshToken savedRefreshToken = refreshTokenRepository.save(refreshToken);

        return new SocialLoginAppResponse(accessToken, savedRefreshToken.getToken());
    }

    private SocialLoginStrategyResponse socialLogin(SocialLoginAppRequest request) {
        SocialLoginStrategy socialLoginStrategy =
                socialLoginStrategyFactory.getSocialLoginStrategy(request.socialType());
        return socialLoginStrategy.login(new SocialLoginStrategyRequest(request.socialAccessToken()));
    }

    @Transactional
    public SocialLoginAppResponse webSocialLogin(SocialLoginStrategyResponse socialLoginStrategyResponse) {
        User findUser = getUser(socialLoginStrategyResponse);

        String accessToken = jwtService.generateToken(findUser.getId());
        RefreshToken refreshToken = RefreshToken.getDefault(findUser);
        RefreshToken savedRefreshToken = refreshTokenRepository.save(refreshToken);

        return new SocialLoginAppResponse(accessToken, savedRefreshToken.getToken());
    }

    private User getUser(SocialLoginStrategyResponse socialLoginStrategyResponse) {
        return userRepository.findBySocialTypeAndSocialId(socialLoginStrategyResponse.socialType().name(), socialLoginStrategyResponse.socialUserId())
                             .orElseGet(() -> {
                                 User savedUser = userRepository.save(createUser(socialLoginStrategyResponse));
                                 createHistory(savedUser.getId(), savedUser);
                                 messageService.send(
                                         "#알림",
                                         "한 명의 회원이 " + socialLoginStrategyResponse.socialType() + "(으)로 가입했습니다!\n" + "이메일: " + socialLoginStrategyResponse.email()
                                 );
                                 return savedUser;
                             });
    }

    private static User createUser(SocialLoginStrategyResponse socialLoginStrategyResponse) {
        return User.builder(
                           socialLoginStrategyResponse.socialType(),
                           socialLoginStrategyResponse.socialUserId()
                   )
                   .profileImage(socialLoginStrategyResponse.profileImage())
                   .email(socialLoginStrategyResponse.email())
                   .gender(socialLoginStrategyResponse.gender())
                   .ageRange(socialLoginStrategyResponse.ageRange())
                   .build();
    }

    private void createHistory(Long requesterId, User targetUser) {
        UserHistory history = UserHistory.from(requesterId, targetUser);
        userHistoryRepository.save(history);
    }
}
