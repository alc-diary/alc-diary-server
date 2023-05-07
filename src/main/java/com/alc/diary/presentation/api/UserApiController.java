package com.alc.diary.presentation.api;

import com.alc.diary.application.user.UserAppService;
import com.alc.diary.application.user.dto.request.CreateRandomNicknameTokenAppRequest;
import com.alc.diary.application.user.dto.request.UpdateAlcoholLimitAndGoalAppRequest;
import com.alc.diary.application.user.dto.request.UpdateNicknameAppRequest;
import com.alc.diary.application.user.dto.request.UpdateUserProfileImageAppRequest;
import com.alc.diary.application.user.dto.response.GetRandomNicknameAppResponse;
import com.alc.diary.application.user.dto.response.GetUserInfoAppResponse;
import com.alc.diary.presentation.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/users")
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

    @PutMapping("/profile-image")
    public ApiResponse<Void> updateUserProfileImage(
            @RequestAttribute Long userId,
            @RequestBody UpdateUserProfileImageAppRequest request
    ) {
        userAppService.updateUserProfileImage(userId, request);
        return ApiResponse.getSuccess();
    }

    @PutMapping("/alcohol-limit-and-goal")
    public ApiResponse<Void> updateAlcoholLimitAndGoal(
            @RequestAttribute Long userId,
            @RequestBody UpdateAlcoholLimitAndGoalAppRequest request
    ) {
        userAppService.updateAlcoholLimitAndGoal(userId, request);
        return ApiResponse.getSuccess();
    }

    @PutMapping("/nickname")
    public ApiResponse<Void> updateNickname(
            @RequestAttribute Long userId,
            @RequestBody UpdateNicknameAppRequest request
    ) {
        userAppService.updateNickname(userId, request);
        return ApiResponse.getSuccess();
    }
}
