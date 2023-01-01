package com.example.alcdiary.presentation.api;

import com.example.alcdiary.application.GetRandomNicknameUseCase;
import com.example.alcdiary.application.SaveUserInfoUseCase;
import com.example.alcdiary.application.command.GetNicknameIsAvailableCommand;
import com.example.alcdiary.application.command.SaveUserInfoCommand;
import com.example.alcdiary.application.result.GetRandomNicknameResult;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/user")
@RestController
public class UserRestController {

    private final GetRandomNicknameUseCase getRandomNicknameUseCase;
    private final SaveUserInfoUseCase saveUserInfoUseCase;

    @GetMapping("/nickname/random")
    public GetRandomNicknameResult getRandomNickname() {
        return getRandomNicknameUseCase.execute();
    }

    @GetMapping("/nickname/valid")
    public void getNicknameIsAvailable(
            @RequestParam String content
    ) {
        GetNicknameIsAvailableCommand command = new GetNicknameIsAvailableCommand(content);

    }

    @PostMapping("/info")
    public void saveUserInfo(
            @RequestHeader("Authorization") String bearerToken,
            @RequestBody SaveUserInfoCommand command
    ) {
        saveUserInfoUseCase.execute(bearerToken, command);
    }
}
