package com.example.alcdiary.application.result;

import com.example.alcdiary.domain.model.RefreshTokenModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class KakaoLoginResult {

    private String accessToken;
    private String refreshToken;

    public static KakaoLoginResult from(String accessToken, RefreshTokenModel refreshTokenModel) {
        KakaoLoginResult result = new KakaoLoginResult();
        result.accessToken = accessToken;
        result.refreshToken = refreshTokenModel.getToken();
        return result;
    }
}
