package com.alc.diary.presentation.api;

import com.alc.diary.application.nickname.NicknameAppService;
import com.alc.diary.application.user.dto.request.CreateRandomNicknameTokenAppRequest;
import com.alc.diary.application.user.dto.response.GetRandomNicknameAppResponse;
import com.alc.diary.presentation.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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
            @RequestBody CreateRandomNicknameTokenAppRequest request
    ) {
        nicknameAppService.createRandomNicknameToken(request);
        return ApiResponse.getCreated();
    }
}
