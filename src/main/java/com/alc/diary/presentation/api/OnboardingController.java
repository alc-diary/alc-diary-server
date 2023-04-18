package com.alc.diary.presentation.api;

import com.alc.diary.application.onboarding.OnboardingAppService;
import com.alc.diary.application.user.UserAppService;
import com.alc.diary.application.user.dto.request.UpdateUserOnboardingInfoAppRequest;
import com.alc.diary.application.user.dto.response.CheckNicknameAvailableAppResponse;
import com.alc.diary.application.user.dto.response.GetRandomNicknameAppResponse;
import com.alc.diary.presentation.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/onboarding")
public class OnboardingController {

    private final OnboardingAppService onboardingAppService;
    private final UserAppService userAppService;

    @GetMapping("/check-nickname-available")
    public ApiResponse<CheckNicknameAvailableAppResponse> checkNicknameAvailable(
        @RequestParam String nickname
    ) {
        return ApiResponse.getSuccess(onboardingAppService.checkNicknameAvailable(nickname));
    }

    @GetMapping("/random-nickname")
    public ApiResponse<GetRandomNicknameAppResponse> getRandomNickname() {
        return ApiResponse.getSuccess(userAppService.getRandomNickname());
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
