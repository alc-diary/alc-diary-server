package com.example.alcdiary.application.command;

import com.example.alcdiary.domain.exception.AlcException;
import com.example.alcdiary.domain.exception.error.AuthError;
import lombok.Getter;

@Getter
public class ReIssueAccessTokenCommand {

    private String bearerToken;

    public ReIssueAccessTokenCommand(String bearerToken) {
        if (!bearerToken.startsWith("Bearer ")) {
            throw new AlcException(AuthError.EXPIRED_REFRESH_TOKEN);
        }
        this.bearerToken = bearerToken;
    }
}
