package com.alc.diary.presentation.api;

import com.alc.diary.application.onboarding.dto.response.GetIsOnboardingDoneAppResponse;
import com.alc.diary.application.user.UserStatusAppService;
import com.alc.diary.presentation.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/v1/user-status")
@RestController
public class UserStatusApiController {

    private final UserStatusAppService userStatusAppService;

    @GetMapping("/is-onboarding-done")
    public ApiResponse<GetIsOnboardingDoneAppResponse> getIsOnboardingDone(
            @RequestAttribute long userId
    ) {
        return ApiResponse.getSuccess(userStatusAppService.getIsOnboardingDone(userId));
    }
}
