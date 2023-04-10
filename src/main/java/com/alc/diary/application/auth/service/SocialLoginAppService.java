package com.alc.diary.application.auth.service;

import com.alc.diary.application.auth.dto.request.SocialLoginAppRequest;
import com.alc.diary.application.auth.strategy.dto.SocialLoginStrategyRequest;
import com.alc.diary.application.auth.dto.response.SocialLoginAppResponse;
import com.alc.diary.application.auth.strategy.dto.SocialLoginStrategyResponse;
import com.alc.diary.application.auth.factory.SocialLoginStrategyFactory;
import com.alc.diary.application.auth.strategy.SocialLoginStrategy;
import com.alc.diary.domain.auth.RefreshToken;
import com.alc.diary.domain.auth.policy.DefaultExpiredPolicy;
import com.alc.diary.domain.auth.repository.RefreshTokenRepository;
import com.alc.diary.domain.user.User;
import com.alc.diary.domain.user.enums.AgeRangeType;
import com.alc.diary.domain.user.enums.DescriptionStyle;
import com.alc.diary.domain.user.enums.GenderType;
import com.alc.diary.domain.user.enums.SocialType;
import com.alc.diary.domain.user.repository.UserRepository;
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
    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public SocialLoginAppResponse login(SocialLoginAppRequest request) {
        SocialLoginStrategy socialLoginStrategy =
                socialLoginStrategyFactory.getSocialLoginStrategy(request.socialType());
        SocialLoginStrategyResponse socialLoginStrategyResponse =
                socialLoginStrategy.login(new SocialLoginStrategyRequest(request.socialAccessToken()));
        User findUser =
            userRepository.findBySocialTypeAndSocialId(request.socialType(), socialLoginStrategyResponse.socialUserId())
                .orElseGet(() -> {
                    User user = User.builder(
                            request.socialType(),
                            socialLoginStrategyResponse.socialUserId(),
                            DescriptionStyle.MILD)
                        .profileImage(socialLoginStrategyResponse.profileImage())
                        .email(socialLoginStrategyResponse.email())
                        .gender(socialLoginStrategyResponse.gender())
                        .ageRange(socialLoginStrategyResponse.ageRange())
                        .build();
                    return userRepository.save(user);
                });

        String accessToken = jwtService.generateToken(findUser.getId());
        RefreshToken refreshToken = RefreshToken.getDefault(findUser);
        RefreshToken savedRefreshToken = refreshTokenRepository.save(refreshToken);

        return new SocialLoginAppResponse(accessToken, savedRefreshToken.getToken());
    }

    @Transactional
    public SocialLoginAppResponse kakaoLogin(KakaoLoginResponse request) {
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
        User findUser =
                userRepository.findBySocialTypeAndSocialId(SocialType.KAKAO, socialLoginStrategyResponse.socialUserId())
                        .orElseGet(() -> {
                            User user = User.builder(
                                            SocialType.KAKAO,
                                            socialLoginStrategyResponse.socialUserId(),
                                            DescriptionStyle.MILD)
                                    .profileImage(socialLoginStrategyResponse.profileImage())
                                    .email(socialLoginStrategyResponse.email())
                                    .gender(socialLoginStrategyResponse.gender())
                                    .ageRange(socialLoginStrategyResponse.ageRange())
                                    .build();
                            return userRepository.save(user);
                        });

        String accessToken = jwtService.generateToken(findUser.getId());
        RefreshToken refreshToken = RefreshToken.getDefault(findUser);
        RefreshToken savedRefreshToken = refreshTokenRepository.save(refreshToken);

        return new SocialLoginAppResponse(accessToken, savedRefreshToken.getToken());
    }
}
