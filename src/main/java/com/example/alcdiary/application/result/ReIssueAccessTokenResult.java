package com.example.alcdiary.application.result;

import com.example.alcdiary.domain.model.RefreshTokenModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReIssueAccessTokenResult {

    private String accessToken;
    private String refreshToken;

    public static ReIssueAccessTokenResult of(String accessToken, RefreshTokenModel refreshTokenModel) {
        ReIssueAccessTokenResult result = new ReIssueAccessTokenResult();
        result.accessToken = accessToken;
        result.refreshToken = refreshTokenModel.getToken();
        return result;
    }
}
