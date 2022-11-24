package com.example.alcdiary.application.impl;

import com.example.alcdiary.application.KakaoLoginUseCase;
import com.example.alcdiary.application.command.KakaoLoginCommand;
import com.example.alcdiary.application.kakao.KakaoAuth;
import com.example.alcdiary.application.kakao.model.KakaoResponse;
import com.example.alcdiary.application.result.KakaoLoginResult;
import com.example.alcdiary.application.util.jwt.JwtProvider;
import com.example.alcdiary.domain.exception.AlcException;
import com.example.alcdiary.domain.model.RefreshTokenModel;
import com.example.alcdiary.domain.model.user.*;
import com.example.alcdiary.domain.service.RefreshTokenService;
import com.example.alcdiary.domain.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
class KakaoLoginUseCaseImpl implements KakaoLoginUseCase {

    private final UserService userService;
    private final KakaoAuth kakaoAuth;
    private final JwtProvider jwtProvider;
    private final RefreshTokenService refreshTokenService;

    @Transactional
    @Override
    public KakaoLoginResult execute(KakaoLoginCommand command) {
        KakaoResponse kakaoResponse = kakaoAuth.getUserInfo(command.getBearerToken());
        UserModel userModel = UserModel.of(
                UserIdModel.of(kakaoResponse.getId(), EUserServiceType.KAKAO),
                kakaoResponse.getKakao_account().getEmail(),
                kakaoResponse.getKakao_account().getProfile().getProfile_image_url(),
                EUserGender.from(kakaoResponse.getKakao_account().getGender()),
                EUserAgeRange.from(kakaoResponse.getKakao_account().getAge_range())
        );
        UserModel findUserModel;
        try {
            findUserModel = userService.getBy(userModel.getId());
        } catch (AlcException e) {
            findUserModel = userService.save(userModel);
        }

        String generatedAccessToken = jwtProvider.generateToken(findUserModel.getId());
        RefreshTokenModel generatedRefreshTokenModel = refreshTokenService.generate(findUserModel);
        return KakaoLoginResult.from(generatedAccessToken, generatedRefreshTokenModel);
    }
}
