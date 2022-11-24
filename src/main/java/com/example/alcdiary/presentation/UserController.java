package com.example.alcdiary.presentation;

import com.example.alcdiary.application.GetRandomNicknameUseCase;
import com.example.alcdiary.application.result.GetRandomNicknameResult;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/user")
@RestController
public class UserController {

    private final GetRandomNicknameUseCase getRandomNicknameUseCase;

    @GetMapping("/nickname/random")
    public GetRandomNicknameResult getRandomNickname() {
        return getRandomNicknameUseCase.execute();
    }
    //
    // @PostMapping("/info")
    // public SaveUserInfoResult saveUserInfo() {
    //     return null;
    // }
}
