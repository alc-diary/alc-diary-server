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
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import springfox.documentation.annotations.ApiIgnore;

import java.util.regex.Pattern;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/onboarding")
public class OnboardingApiController {

    private static final Pattern NICKNAME_PATTERN = Pattern.compile("^[a-zA-Z0-9가-힣]+$");

    private final OnboardingAppService onboardingAppService;
    private final NicknameAppService nicknameAppService;
    private final UserStatusAppService userStatusAppService;

    @GetMapping("/is-onboarding-done")
    public ApiResponse<GetIsOnboardingDoneAppResponse> getIsOnboardingDone(
            @ApiIgnore @RequestAttribute Long userId
    ) {
        return ApiResponse.getSuccess(userStatusAppService.getIsOnboardingDone(userId));
    }

    @GetMapping("/check-nickname-available")
    public ApiResponse<CheckNicknameAvailableAppResponse> checkNicknameAvailable(
        @RequestParam @Validated String nickname
    ) {
        if (nickname == null || !NICKNAME_PATTERN.matcher(nickname).matches()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "닉네임은 한글, 영어 대소문자, 숫자만 가능합니다.");
        }
        return ApiResponse.getSuccess(onboardingAppService.checkNicknameAvailable(nickname));
    }

    @GetMapping("/random-nickname")
    public ApiResponse<GetRandomNicknameAppResponse> getRandomNickname() {
        return ApiResponse.getSuccess(nicknameAppService.getRandomNickname());
    }

    @PutMapping("/user-info")
    public ApiResponse<Void> updateUserOnboardingInfo(
        @ApiIgnore @RequestAttribute long userId,
        @Validated @RequestBody UpdateUserOnboardingInfoAppRequest request
        ) {
        onboardingAppService.updateUserOnboardingInfo(userId, request);
        return ApiResponse.getSuccess();
    }
}
