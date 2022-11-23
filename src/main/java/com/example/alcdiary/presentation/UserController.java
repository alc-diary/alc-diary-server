package com.example.alcdiary.presentation;

import com.example.alcdiary.application.KakaoLoginUseCase;
import com.example.alcdiary.application.command.KakaoLoginCommand;
import com.example.alcdiary.domain.model.user.UserModel;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/user")
@RestController
public class UserController {

    private final KakaoLoginUseCase kakaoLoginUseCase;

    @PostMapping("/login/kakao")
    public UserModel kakaoLogin(
            @RequestHeader("Authorization") String bearerToken
    ) {
        KakaoLoginCommand kakaoLoginCommand = new KakaoLoginCommand(bearerToken);
        return kakaoLoginUseCase.execute(kakaoLoginCommand);
    }
}
