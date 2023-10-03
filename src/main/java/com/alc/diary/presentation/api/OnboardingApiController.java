package com.alc.diary.presentation.api;

import com.alc.diary.application.nickname.NicknameAppService;
import com.alc.diary.application.onboarding.OnboardingAppService;
import com.alc.diary.application.onboarding.dto.response.GetIsOnboardingDoneAppResponse;
import com.alc.diary.application.user.UserStatusAppService;
import com.alc.diary.application.user.dto.request.UpdateUserOnboardingInfoAppRequest;
import com.alc.diary.application.user.dto.response.CheckNicknameAvailableAppResponse;
import com.alc.diary.application.user.dto.response.GetRandomNicknameAppResponse;
import com.alc.diary.domain.exception.DomainException;
import com.alc.diary.domain.user.UserDetail;
import com.alc.diary.domain.user.error.UserError;
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

    private final OnboardingAppService onboardingAppService;
    private final NicknameAppService nicknameAppService;
    private final UserStatusAppService userStatusAppService;

    /**
     * 온보딩 완료 여부를 가져온다.
     *
     * @param userId 사용자 ID
     * @return 온보딩 완료 여부
     */
    @GetMapping("/is-onboarding-done")
    public ApiResponse<GetIsOnboardingDoneAppResponse> getIsOnboardingDone(
            @ApiIgnore @RequestAttribute Long userId
    ) {
        return ApiResponse.getSuccess(userStatusAppService.getIsOnboardingDone(userId));
    }

    /**
     * 닉네임 중복 체크
     *
     * @param nickname 닉네임
     * @return 중복 여부
     */
    @GetMapping("/check-nickname-available")
    public ApiResponse<CheckNicknameAvailableAppResponse> checkNicknameAvailable(
        @RequestParam String nickname
    ) {
        if (nickname == null || !UserDetail.NICKNAME_PATTERN.matcher(nickname).matches()) {
            throw new DomainException(UserError.INVALID_NICKNAME_FORMAT, "닉네임은 한글, 영어 대소문자, 숫자만 가능합니다.");
        }
        return ApiResponse.getSuccess(onboardingAppService.checkNicknameAvailable(nickname));
    }

    /**
     * 랜덤 닉네임을 가져온다.
     *
     * @return 랜덤 닉네임
     */
    @GetMapping("/random-nickname")
    public ApiResponse<GetRandomNicknameAppResponse> getRandomNickname() {
        return ApiResponse.getSuccess(nicknameAppService.getRandomNickname());
    }

    /**
     * 닉네임 토큰을 생성한다.
     *
     * @param userId 사용자 ID
     * @param request 닉네임 토큰 생성 요청
     * @return 성공
     */
    @PutMapping("/user-info")
    public ApiResponse<Void> updateUserOnboardingInfo(
        @ApiIgnore @RequestAttribute long userId,
        @Validated @RequestBody UpdateUserOnboardingInfoAppRequest request
        ) {
        onboardingAppService.updateUserOnboardingInfo(userId, request);
        return ApiResponse.getSuccess();
    }
}
