package com.example.alcdiary.application.result;

import com.example.alcdiary.domain.model.token.AccessTokenModel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReissueAccessTokenResult {

    private String accessToken;

    public static ReissueAccessTokenResult from(AccessTokenModel accessTokenModel) {
        ReissueAccessTokenResult reissueAccessTokenResult = new ReissueAccessTokenResult();
        reissueAccessTokenResult.accessToken = accessTokenModel.getToken();
        return reissueAccessTokenResult;
    }
}
