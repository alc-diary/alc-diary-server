package com.example.alcdiary.presentation.dto.request;

import com.example.alcdiary.application.command.LoginCommand;
import com.example.alcdiary.domain.enums.SocialType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class KakaoLoginRequest {

    private String token;

    public LoginCommand toLoginCommand() {
        return new LoginCommand(
                SocialType.KAKAO,
                token
        );
    }
}
