package com.example.alcdiary.application;

import com.example.alcdiary.application.command.KakaoLoginCommand;
import com.example.alcdiary.application.kakao.model.KakaoResponse;
import com.example.alcdiary.application.result.KakaoLoginResult;
import com.example.alcdiary.domain.model.user.UserModel;

public interface KakaoLoginUseCase {

    // KakaoLoginResult execute(KakaoLoginCommand command);
    UserModel execute(KakaoLoginCommand command);
}
