package com.example.alcdiary.application.result;

import lombok.Getter;

@Getter
public class GetRandomNicknameResult {

    private String nickname;

    public static GetRandomNicknameResult from(String nickname) {
        GetRandomNicknameResult result = new GetRandomNicknameResult();
        result.nickname = nickname;
        return result;
    }
}
