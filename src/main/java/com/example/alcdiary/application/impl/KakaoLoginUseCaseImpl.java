package com.example.alcdiary.application.impl;

import com.example.alcdiary.application.KakaoLoginUseCase;
import com.example.alcdiary.application.command.KakaoLoginCommand;
import com.example.alcdiary.application.kakao.KakaoAuth;
import com.example.alcdiary.application.kakao.model.KakaoResponse;
import com.example.alcdiary.domain.exception.AlcException;
import com.example.alcdiary.domain.model.user.EUserServiceType;
import com.example.alcdiary.domain.model.user.UserIdModel;
import com.example.alcdiary.domain.model.user.UserModel;
import com.example.alcdiary.domain.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class KakaoLoginUseCaseImpl implements KakaoLoginUseCase {

    private final UserService userService;
    private final KakaoAuth kakaoAuth;

    @Transactional
    @Override
    public UserModel execute(KakaoLoginCommand command) {
        KakaoResponse kakaoResponse = kakaoAuth.getUserInfo(command.getBearerToken());
        UserModel userModel = UserModel.builder()
                .id(new UserIdModel(kakaoResponse.getId(), EUserServiceType.KAKAO))
                .email(kakaoResponse.getKakao_account().getEmail())
                .nickname("nickname")
                .profileImageUrl(kakaoResponse.getKakao_account().getProfile().getProfile_image_url())
                .build();
        try {
            userModel =  userService.getBy(userModel);
        } catch (AlcException e) {
            userModel = userService.saveDefault(userModel);
        }
        return userModel;
    }
}
