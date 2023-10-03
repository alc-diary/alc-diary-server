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

    /**
     * 랜덤 닉네임을 가져온다.
     *
     * @return 랜덤 닉네임
     */
    @GetMapping("/random")
    public ApiResponse<GetRandomNicknameAppResponse> getRandomNickname() {
        return ApiResponse.getSuccess(nicknameAppService.getRandomNickname());
    }

    /**
     * 랜덤 닉네임 토큰을 생성한다.
     *
     * @param request 랜덤 닉네임 토큰 생성 요청
     * @return 성공
     */
    @PostMapping("/tokens")
    public ApiResponse<Void> createNicknameToken(
            @Validated @RequestBody CreateRandomNicknameTokenAppRequest request
    ) {
        nicknameAppService.createRandomNicknameToken(request);
        return ApiResponse.getCreated();
    }

    /**
     * 금칙어를 추가한다.
     *
     * @param userId 사용자 ID
     * @param request 금칙어 추가 요청
     * @return 금칙어 ID
     */
    @PostMapping("/banned-words")
    public ApiResponse<Long> addBannedWords(
            @ApiIgnore @RequestAttribute long userId,
            @Validated @RequestBody AddNicknameBannedWordRequest request
    ) {
        return ApiResponse.getSuccess(nicknameAppService.addNicknameBannedWord(userId, request));
    }

    /**
     * 금칙어를 삭제한다.
     *
     * @param userId 사용자 ID
     * @param bannedWordId 금칙어 ID
     * @return 성공
     */
    @DeleteMapping("/banned-words/{bannedWordId}")
    public ApiResponse<Void> deleteBannedWord(
            @ApiIgnore @RequestAttribute long userId,
            @PathVariable long bannedWordId
    ) {
        nicknameAppService.deleteNicknameBannedWord(userId, bannedWordId);
        return ApiResponse.getSuccess();
    }
}
