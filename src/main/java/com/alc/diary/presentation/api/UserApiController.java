package com.alc.diary.presentation.api;

import com.alc.diary.application.nickname.NicknameAppService;
import com.alc.diary.application.user.UserAppService;
import com.alc.diary.application.user.dto.request.*;
import com.alc.diary.application.user.dto.response.GetRandomNicknameAppResponse;
import com.alc.diary.application.user.dto.response.GetUserInfoAppResponse;
import com.alc.diary.application.user.dto.response.SearchUserAppResponse;
import com.alc.diary.domain.exception.DomainException;
import com.alc.diary.domain.user.UserDetail;
import com.alc.diary.domain.user.error.UserError;
import com.alc.diary.presentation.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import springfox.documentation.annotations.ApiIgnore;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/users")
public class UserApiController {

    private final UserAppService userAppService;
    private final NicknameAppService nicknameAppService;

    /**
     * 닉네임으로 사용자 검색
     *
     * @param nickname 닉네임
     * @return 사용자 검색 결과
     */
    @GetMapping
    public ApiResponse<SearchUserAppResponse> searchUser(
            @RequestParam String nickname
    ) {
        if (nickname == null || !UserDetail.NICKNAME_PATTERN.matcher(nickname).matches()) {
            throw new DomainException(UserError.INVALID_NICKNAME_FORMAT);
        }
        return ApiResponse.getSuccess(userAppService.searchUser(nickname));
    }

    /**
     * 사용자 정보 조회
     *
     * @param userId 사용자 ID
     * @return 사용자 정보
     */
    @GetMapping("/info")
    public ApiResponse<GetUserInfoAppResponse> getUserInfo(
            @ApiIgnore @RequestAttribute Long userId
    ) {
        return ApiResponse.getSuccess(userAppService.getUserInfo(userId));
    }

    /**
     * 닉네임 토큰 생성
     *
     * @param request 닉네임 토큰 생성 요청
     * @return 성공
     */
    @PostMapping("/nickname-token")
    public ApiResponse<Void> createNicknameToken(
            @Validated @RequestBody CreateRandomNicknameTokenAppRequest request
    ) {
        nicknameAppService.createRandomNicknameToken(request);
        return ApiResponse.getCreated();
    }

    /**
     * 닉네임 토큰 조회
     *
     * @return 닉네임 토큰
     */
    @GetMapping("/nickname-token")
    public ApiResponse<GetRandomNicknameAppResponse> getRandomNickname() {
        return ApiResponse.getSuccess(nicknameAppService.getRandomNickname());
    }

    /**
     * 사용자 프로필 이미지 수정
     *
     * @param userId 사용자 ID
     * @param request 사용자 프로필 이미지 수정 요청
     * @return 성공
     */
    @PutMapping("/profile-image")
    public ApiResponse<Void> updateUserProfileImage(
            @ApiIgnore @RequestAttribute Long userId,
            @Validated @RequestBody UpdateUserProfileImageAppRequest request
    ) {
        userAppService.updateUserProfileImage(userId, request);
        return ApiResponse.getSuccess();
    }

    /**
     * 주량과 금주 목표량 수정
     *
     * @param userId 사용자 ID
     * @param request 주량과 금주 목표량 수정 요청
     * @return 성공
     */
    @PutMapping("/alcohol-limit-and-goal")
    public ApiResponse<Void> updateAlcoholLimitAndGoal(
            @ApiIgnore @RequestAttribute Long userId,
            @Validated @RequestBody UpdateAlcoholLimitAndGoalAppRequest request
    ) {
        userAppService.updateAlcoholLimitAndGoal(userId, request);
        return ApiResponse.getSuccess();
    }

    /**
     * 사용자 닉네임 수정
     *
     * @param userId 사용자 ID
     * @param request 사용자 닉네임 수정 요청
     * @return 성공
     */
    @PutMapping("/nickname")
    public ApiResponse<Void> updateNickname(
            @ApiIgnore @RequestAttribute Long userId,
            @Validated @RequestBody UpdateNicknameAppRequest request
    ) {
        userAppService.updateNickname(userId, request);
        return ApiResponse.getSuccess();
    }

    /**
     * 앱 설명 타입 수정
     *
     * @param userId 사용자 ID
     * @param request 앱 설명 타입 수정 요청
     * @return 성공
     */
    @PutMapping("/description-style")
    public ApiResponse<Void> updateDescriptionStyle(
            @ApiIgnore @RequestAttribute Long userId,
            @Validated @RequestBody UpdateDescriptionStyleAppRequest request
    ) {
        userAppService.updateDescriptionStyle(userId, request);
        return ApiResponse.getSuccess();
    }

    /**
     * 사용자 비활성화
     *
     * @param requesterId 요청자 ID
     * @param request 사용자 비활성화 요청
     * @return 성공
     */
    @PutMapping("/deactivate")
    public ApiResponse<Void> deactivateUser(
            @ApiIgnore @RequestAttribute("userId") Long requesterId,
            @Validated @RequestBody DeactivateUserAppRequest request
    ) {
        userAppService.deactivateUser(requesterId, request);
        return ApiResponse.getSuccess();
    }

    /**
     * 푸시 알림 활성화
     *
     * @param userId
     * @return
     */
    @PutMapping("/notification-settings/enable")
    public ApiResponse<Void> enableNotificationSetting(
            @ApiIgnore @RequestAttribute("userId") Long userId
    ) {
        userAppService.enableNotificationSetting(userId);
        return ApiResponse.getSuccess();
    }

    /**
     * 푸시 알림 비 활성화
     *
     * @param userId
     * @return
     */
    @PutMapping("/notification-settings/disable")
    public ApiResponse<Void> disableNotificationSetting(
            @ApiIgnore @RequestAttribute("userId") Long userId
    ) {
        userAppService.disableNotificationSetting(userId);
        return ApiResponse.getSuccess();
    }
}
