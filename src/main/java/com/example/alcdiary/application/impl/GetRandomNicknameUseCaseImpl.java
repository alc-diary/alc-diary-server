package com.example.alcdiary.application.impl;

import com.example.alcdiary.application.GetRandomNicknameUseCase;
import com.example.alcdiary.application.result.GetRandomNicknameResult;
import com.example.alcdiary.application.util.keyword.KeywordUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
class GetRandomNicknameUseCaseImpl implements GetRandomNicknameUseCase {

    private final KeywordUtils keywordUtils;

    @Override
    public GetRandomNicknameResult execute() {
        String nickname = keywordUtils.generateNickname();
        return GetRandomNicknameResult.from(nickname);
    }
}
