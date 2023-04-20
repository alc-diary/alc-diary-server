package com.alc.diary.presentation.api;

import com.alc.diary.application.user.UserAppService;
import com.alc.diary.application.user.dto.request.CreateRandomNicknameTokenAppRequest;
import com.alc.diary.application.user.dto.response.GetRandomNicknameAppResponse;
import com.alc.diary.application.user.dto.response.GetUserInfoAppResponse;
import com.alc.diary.presentation.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/user")
public class UserApiController {

    private final UserAppService userAppService;

    @GetMapping("/info")
    public ApiResponse<GetUserInfoAppResponse> getUserInfo(
        @RequestAttribute Long userId
    ) {
        return ApiResponse.getSuccess(userAppService.getUser(userId));
    }

    @PostMapping("/nickname-token")
    public ApiResponse<Void> createNicknameToken(
        @RequestBody CreateRandomNicknameTokenAppRequest request
    ) {
        userAppService.createRandomNicknameToken(request);
        return ApiResponse.getCreated();
    }

    @GetMapping("/nickname-token")
    public ApiResponse<GetRandomNicknameAppResponse> getRandomNickname() {
        return ApiResponse.getSuccess(userAppService.getRandomNickname());
    }
}
