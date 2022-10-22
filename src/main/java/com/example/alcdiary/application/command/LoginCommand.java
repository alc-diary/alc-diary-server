package com.example.alcdiary.application.command;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class LoginCommand {

    private Service service;
    private String token;

    public enum Service {

        KAKAO,
        GOOGLE,
        ALC,
    }
}
