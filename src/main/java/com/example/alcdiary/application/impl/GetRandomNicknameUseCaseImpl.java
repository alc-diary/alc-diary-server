package com.example.alcdiary.application.impl;

import com.example.alcdiary.application.GetRandomNicknameUseCase;
import com.example.alcdiary.application.result.GetRandomNicknameResult;
import com.example.alcdiary.application.util.nickname.NicknameUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
class GetRandomNicknameUseCaseImpl implements GetRandomNicknameUseCase {

    private final NicknameUtils nicknameUtils;

    @Override
    public GetRandomNicknameResult execute() {
        String nickname = nicknameUtils.generateNickname();
        return GetRandomNicknameResult.from(nickname);
    }
}
