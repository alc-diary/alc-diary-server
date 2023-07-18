package com.alc.diary.presentation.api;

import com.alc.diary.application.nickname.NicknameAppService;
import com.alc.diary.application.nickname.dto.request.AddNicknameBannedWordRequest;
import com.alc.diary.application.user.dto.request.CreateRandomNicknameTokenAppRequest;
import com.alc.diary.application.user.dto.response.GetRandomNicknameAppResponse;
import com.alc.diary.presentation.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

@RequiredArgsConstructor
@RequestMapping("/v1/nicknames")
@RestController
public class NicknameApiController {

    private final NicknameAppService nicknameAppService;

    @GetMapping("/random")
    public ApiResponse<GetRandomNicknameAppResponse> getRandomNickname() {
        return ApiResponse.getSuccess(nicknameAppService.getRandomNickname());
    }

    @PostMapping("/tokens")
    public ApiResponse<Void> createNicknameToken(
            @Validated @RequestBody CreateRandomNicknameTokenAppRequest request
    ) {
        nicknameAppService.createRandomNicknameToken(request);
        return ApiResponse.getCreated();
    }

    @PostMapping("/banned-words")
    public ApiResponse<Long> addBannedWords(
            @ApiIgnore @RequestAttribute long userId,
            @Validated @RequestBody AddNicknameBannedWordRequest request
    ) {
        return ApiResponse.getSuccess(nicknameAppService.addNicknameBannedWord(userId, request));
    }

    @DeleteMapping("/banned-words/{bannedWordId}")
    public ApiResponse<Void> deleteBannedWord(
            @ApiIgnore @RequestAttribute long userId,
            @PathVariable long bannedWordId
    ) {
        nicknameAppService.deleteNicknameBannedWord(userId, bannedWordId);
        return ApiResponse.getSuccess();
    }
}
