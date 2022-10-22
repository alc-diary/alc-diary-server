package com.example.alcdiary.infrastructure.adapter;

import com.example.alcdiary.application.command.LoginCommand;
import com.example.alcdiary.application.port.AuthPort;
import com.example.alcdiary.domain.model.UserModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AuthAdapter implements AuthPort {

    private LoginCommand.Service service;
    private String token;

    @Override
    public AuthPort service(LoginCommand.Service service) {
        this.service = service;
        return this;
    }

    @Override
    public AuthPort token(String token) {
        this.token = token;
        return this;
    }

    @Override
    public UserModel authentication() {
        switch (service) {
            case KAKAO:
                return UserModel.builder()
                        .id("id")
                        .username("taeyeong")
                        .gender(UserModel.Gender.MAN)
                        .profileImgUrl("profileImgUrl")
                        .build();
            case GOOGLE:
                System.out.println("google");
                break;
            case ALC:
                System.out.println("alc");
                break;
        }
        return null;
    }
}
