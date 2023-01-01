package com.example.alcdiary.presentation.api;

import com.example.alcdiary.application.GetNicknameIsAvailableUseCase;
import com.example.alcdiary.application.GetRandomNicknameUseCase;
import com.example.alcdiary.application.SaveUserInfoUseCase;
import com.example.alcdiary.application.command.GetNicknameIsAvailableCommand;
import com.example.alcdiary.application.command.SaveUserInfoCommand;
import com.example.alcdiary.application.result.GetNicknameIsAvailableResult;
import com.example.alcdiary.application.result.GetRandomNicknameResult;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/user")
@RestController
public class UserRestController {

    private final GetRandomNicknameUseCase getRandomNicknameUseCase;
    private final SaveUserInfoUseCase saveUserInfoUseCase;
    private final GetNicknameIsAvailableUseCase getNicknameIsAvailableUseCase;

    @GetMapping("/nickname/random")
    public GetRandomNicknameResult getRandomNickname() {
        return getRandomNicknameUseCase.execute();
    }

    @GetMapping("/nickname/valid")
    public ResponseEntity<GetNicknameIsAvailableResult> getNicknameIsAvailable(
            @RequestParam String content
    ) {
        GetNicknameIsAvailableCommand command = new GetNicknameIsAvailableCommand(content);
        return ResponseEntity.ok(
                getNicknameIsAvailableUseCase.execute(command)
        );
    }

    @PostMapping("/info")
    public void saveUserInfo(
            @RequestHeader("Authorization") String bearerToken,
            @RequestBody SaveUserInfoCommand command
    ) {
        saveUserInfoUseCase.execute(bearerToken, command);
    }
}
