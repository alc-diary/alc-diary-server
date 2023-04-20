package com.alc.diary.application.auth.strategy;

import com.alc.diary.application.auth.strategy.dto.SocialLoginStrategyRequest;
import com.alc.diary.application.auth.strategy.dto.SocialLoginStrategyResponse;
import com.alc.diary.domain.user.enums.AgeRangeType;
import com.alc.diary.domain.user.enums.GenderType;
import com.alc.diary.domain.user.enums.SocialType;
import com.alc.diary.infrastructure.external.client.feign.kakao.KakaoFeignClient;
import com.alc.diary.infrastructure.external.client.feign.kakao.dto.response.KakaoLoginResponse;
import com.alc.diary.infrastructure.external.client.feign.kakao.dto.response.KakaoLoginResponse.KakaoAccount;
import com.alc.diary.infrastructure.external.client.feign.kakao.dto.response.KakaoLoginResponse.KakaoAccount.Profile;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.alc.diary.domain.user.enums.AgeRangeType.*;

@RequiredArgsConstructor
@Service
public class KakaoLoginService implements SocialLoginStrategy {
    private final KakaoFeignClient kakaoFeignClient;

    @Override
    public SocialLoginStrategyResponse login(SocialLoginStrategyRequest request) {
        String bearerToken = "Bearer " + request.socialAccessToken();
        ResponseEntity<KakaoLoginResponse> kakaoResponseEntity = kakaoFeignClient.kakaoLogin(bearerToken);
        if (kakaoResponseEntity.getStatusCode().isError()) {
            throw new IllegalArgumentException("Kakao authentication error");
        }
        KakaoLoginResponse kakaoResponse = kakaoResponseEntity.getBody();
        assert kakaoResponse != null;
        Optional<KakaoAccount> kakaoAccountOptional = Optional.of(kakaoResponse.getKakaoAccount());
        Optional<Profile> profileOptional = kakaoAccountOptional.map(KakaoAccount::getProfile);

        String profileImageUrl = profileOptional.map(Profile::getProfileImageUrl).orElse(null);
        String email = kakaoAccountOptional.map(KakaoAccount::getEmail).orElse(null);
        GenderType gender = kakaoAccountOptional
            .map(KakaoAccount::getGender)
            .map(s -> switch (s) {
                case "male" -> GenderType.MALE;
                case "female" -> GenderType.FEMALE;
                default -> throw new IllegalArgumentException("Invalid gender: " + s);
            })
            .orElse(null);
        AgeRangeType ageRange = kakaoAccountOptional.map(KakaoAccount::getAgeRange)
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

        return new SocialLoginStrategyResponse(
            SocialType.KAKAO,
            String.valueOf(kakaoResponse.getId()),
            profileImageUrl,
            email,
            gender,
            ageRange
        );
    }
}
