package com.alc.diary.presentation.api;

import com.alc.diary.application.nickname.NicknameAppService;
import com.alc.diary.application.onboarding.OnboardingAppService;
import com.alc.diary.application.onboarding.dto.response.GetIsOnboardingDoneAppResponse;
import com.alc.diary.application.user.UserStatusAppService;
import com.alc.diary.application.user.dto.request.UpdateUserOnboardingInfoAppRequest;
import com.alc.diary.application.user.dto.response.CheckNicknameAvailableAppResponse;
import com.alc.diary.application.user.dto.response.GetRandomNicknameAppResponse;
import com.alc.diary.presentation.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/onboarding")
public class OnboardingApiController {

    private final OnboardingAppService onboardingAppService;
    private final NicknameAppService nicknameAppService;
    private final UserStatusAppService userStatusAppService;

    @GetMapping("/is-onboarding-done")
    public ApiResponse<GetIsOnboardingDoneAppResponse> getIsOnboardingDone(
            @RequestAttribute Long userId
    ) {
        return ApiResponse.getSuccess(userStatusAppService.getIsOnboardingDone(userId));
    }

    @GetMapping("/check-nickname-available")
    public ApiResponse<CheckNicknameAvailableAppResponse> checkNicknameAvailable(
        @RequestParam String nickname
    ) {
        return ApiResponse.getSuccess(onboardingAppService.checkNicknameAvailable(nickname));
    }

    @GetMapping("/random-nickname")
    public ApiResponse<GetRandomNicknameAppResponse> getRandomNickname() {
        return ApiResponse.getSuccess(nicknameAppService.getRandomNickname());
    }

    @PutMapping("/user-info")
    public ApiResponse<Void> updateUserOnboardingInfo(
        @RequestAttribute Long userId,
        @RequestBody UpdateUserOnboardingInfoAppRequest request
        ) {
        onboardingAppService.updateUserOnboardingInfo(userId, request);
        return ApiResponse.getSuccess();
    }
}
