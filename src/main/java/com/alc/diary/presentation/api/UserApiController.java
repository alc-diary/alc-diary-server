package com.alc.diary.presentation.api;

import com.alc.diary.application.nickname.NicknameAppService;
import com.alc.diary.application.user.UserAppService;
import com.alc.diary.application.user.dto.request.*;
import com.alc.diary.application.user.dto.response.GetRandomNicknameAppResponse;
import com.alc.diary.application.user.dto.response.GetUserInfoAppResponse;
import com.alc.diary.application.user.dto.response.SearchUserAppResponse;
import com.alc.diary.presentation.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/users")
public class UserApiController {

    private final UserAppService userAppService;
    private final NicknameAppService nicknameAppService;

    @GetMapping
    public ApiResponse<SearchUserAppResponse> searchUser(
            @RequestParam String nickname
    ) {
        return ApiResponse.getSuccess(userAppService.searchUser(nickname));
    }

    @GetMapping("/info")
    public ApiResponse<GetUserInfoAppResponse> getUserInfo(
            @ApiIgnore @RequestAttribute Long userId
    ) {
        return ApiResponse.getSuccess(userAppService.getUserInfo(userId));
    }

    @PostMapping("/nickname-token")
    public ApiResponse<Void> createNicknameToken(
            @Validated @RequestBody CreateRandomNicknameTokenAppRequest request
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
            @ApiIgnore @RequestAttribute Long userId,
            @Validated @RequestBody UpdateUserProfileImageAppRequest request
    ) {
        userAppService.updateUserProfileImage(userId, request);
        return ApiResponse.getSuccess();
    }

    @PutMapping("/alcohol-limit-and-goal")
    public ApiResponse<Void> updateAlcoholLimitAndGoal(
            @ApiIgnore @RequestAttribute Long userId,
            @Validated @RequestBody UpdateAlcoholLimitAndGoalAppRequest request
    ) {
        userAppService.updateAlcoholLimitAndGoal(userId, request);
        return ApiResponse.getSuccess();
    }

    @PutMapping("/nickname")
    public ApiResponse<Void> updateNickname(
            @ApiIgnore @RequestAttribute Long userId,
            @Validated @RequestBody UpdateNicknameAppRequest request
    ) {
        userAppService.updateNickname(userId, request);
        return ApiResponse.getSuccess();
    }

    @PutMapping("/description-style")
    public ApiResponse<Void> updateDescriptionStyle(
            @ApiIgnore @RequestAttribute Long userId,
            @Validated @RequestBody UpdateDescriptionStyleAppRequest request
    ) {
        userAppService.updateDescriptionStyle(userId, request);
        return ApiResponse.getSuccess();
    }

    @PutMapping("/deactivate")
    public ApiResponse<Void> deactivateUser(
            @ApiIgnore @RequestAttribute("userId") Long requesterId,
            @Validated @RequestBody DeactivateUserAppRequest request
    ) {
        userAppService.deactivateUser(requesterId, request);
        return ApiResponse.getSuccess();
    }
}
