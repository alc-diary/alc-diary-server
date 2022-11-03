package com.example.alcdiary.application.command;

import com.example.alcdiary.domain.model.UserModel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class LoginCommand {

    private UserModel.SocialType socialType;
    private String token;
}
