package com.alc.diary.presentation.api;

import com.alc.diary.application.onboarding.dto.response.GetIsOnboardingDoneAppResponse;
import com.alc.diary.application.user.UserStatusAppService;
import com.alc.diary.presentation.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@RequiredArgsConstructor
@RequestMapping("/v1/user-status")
@RestController
public class UserStatusApiController {

    private final UserStatusAppService userStatusAppService;

    /**
     * 온보딩 완료 여부를 가져온다.
     *
     * @param userId 사용자 ID
     * @return 온보딩 완료 여부
     */
    @GetMapping("/is-onboarding-done")
    public ApiResponse<GetIsOnboardingDoneAppResponse> getIsOnboardingDone(
            @ApiIgnore @RequestAttribute long userId
    ) {
        return ApiResponse.getSuccess(userStatusAppService.getIsOnboardingDone(userId));
    }
}
