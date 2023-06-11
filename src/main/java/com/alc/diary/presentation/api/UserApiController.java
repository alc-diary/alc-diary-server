package com.alc.diary.presentation.api;

import com.alc.diary.application.nickname.NicknameAppService;
import com.alc.diary.application.user.UserAppService;
import com.alc.diary.application.user.dto.request.*;
import com.alc.diary.application.user.dto.response.GetRandomNicknameAppResponse;
import com.alc.diary.application.user.dto.response.GetUserInfoAppResponse;
import com.alc.diary.presentation.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/users")
public class UserApiController {

    private final UserAppService userAppService;
    private final NicknameAppService nicknameAppService;

    @GetMapping("/info")
    public ApiResponse<GetUserInfoAppResponse> getUserInfo(
            @RequestAttribute Long userId
    ) {
        return ApiResponse.getSuccess(userAppService.getUserInfo(userId));
    }

    @PostMapping("/nickname-token")
    public ApiResponse<Void> createNicknameToken(
            @RequestBody CreateRandomNicknameTokenAppRequest request
    ) {
        nicknameAppService.createRandomNicknameToken(request);
        return ApiResponse.getCreated();
    }

    @GetMapping("/nickname-token")
    public ApiResponse<GetRandomNicknameAppResponse> getRandomNickname() {
        return ApiResponse.getSuccess(nicknameAppService.getRandomNickname());
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

    @PutMapping("/description-style")
    public ApiResponse<Void> updateDescriptionStyle(
            @RequestAttribute Long userId,
            @RequestBody UpdateDescriptionStyleAppRequest request
    ) {
        userAppService.updateDescriptionStyle(userId, request);
        return ApiResponse.getSuccess();
    }

    @PutMapping("/deactivate")
    public ApiResponse<Void> deactivateUser(
            @RequestAttribute("userId") Long requesterId,
            @RequestBody DeactivateUserAppRequest request
    ) {
        userAppService.deactivateUser(requesterId, request);
        return ApiResponse.getSuccess();
    }
}
