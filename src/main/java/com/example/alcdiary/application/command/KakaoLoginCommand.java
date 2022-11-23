package com.example.alcdiary.application.command;

import lombok.Getter;

@Getter
public class KakaoLoginCommand {

    private String bearerToken;

    public KakaoLoginCommand(String bearerToken) {
        this.bearerToken = bearerToken;
    }
}
