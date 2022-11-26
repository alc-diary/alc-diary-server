package com.example.alcdiary.application.command;

import com.example.alcdiary.domain.exception.AlcException;
import com.example.alcdiary.domain.exception.error.AuthError;
import lombok.Getter;

@Getter
public class KakaoLoginCommand {

    private String bearerToken;

    public KakaoLoginCommand(String bearerToken) {
        if (!bearerToken.startsWith("Bearer ")) {
            throw new AlcException(AuthError.INVALID_KAKAO_TOKEN);
        }
        this.bearerToken = bearerToken;
    }
}
