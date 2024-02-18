package com.alc.diary.presentation.api;

import com.alc.diary.application.nickname.NicknameAppService;
import com.alc.diary.application.user.LogoutAppService;
import com.alc.diary.application.user.UserServiceV1;
import com.alc.diary.application.user.UserPublicDto;
import com.alc.diary.application.user.dto.request.*;
import com.alc.diary.application.user.dto.response.*;
import com.alc.diary.domain.exception.DomainException;
import com.alc.diary.domain.user.UserDetail;
import com.alc.diary.domain.user.error.UserError;
import com.alc.diary.presentation.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/users")
public class UserApiControllerV1 {

    private final UserServiceV1 userServiceV1;
    private final NicknameAppService nicknameAppService;
    private final LogoutAppService logoutAppService;

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
        return ApiResponse.getSuccess(userServiceV1.searchUser(nickname));
    }

    @GetMapping("/{userId}")
    public ApiResponse<UserPublicDto> getUserById(@PathVariable Long userId) {
        return ApiResponse.getSuccess(userServiceV1.getUserById(userId));
    }

    @GetMapping("/batch")
    public ApiResponse<List<UserPublicDto>> getUsersByIds(@RequestParam List<Long> userIds) {
        return ApiResponse.getSuccess(userServiceV1.getUsersByIds(userIds));
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
        return ApiResponse.getSuccess(userServiceV1.getUserInfo(userId));
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
        userServiceV1.updateUserProfileImage(userId, request);
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
        userServiceV1.updateAlcoholLimitAndGoal(userId, request);
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
        userServiceV1.updateNickname(userId, request);
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
        userServiceV1.updateDescriptionStyle(userId, request);
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
        userServiceV1.deactivateUser(requesterId, request);
        return ApiResponse.getSuccess();
    }

    /**
     * 푸시 알림 활성화 여부 조회
     *
     * @param userId
     * @return
     */
    @GetMapping("/notification-settings")
    public ApiResponse<GetNotificationSettingAppResponse> getNotificationSetting(
            @ApiIgnore @RequestAttribute("userId") Long userId
    ) {
        return ApiResponse.getSuccess(userServiceV1.getNotificationSetting(userId));
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
        userServiceV1.enableNotificationSetting(userId);
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
        userServiceV1.disableNotificationSetting(userId);
        return ApiResponse.getSuccess();
    }

    /**
     * 로그아웃
     *
     * @param userId
     * @return
     */
    @PostMapping("/logout")
    public ApiResponse<Void> logout(
            @ApiIgnore @RequestAttribute("userId") Long userId
    ) {
        logoutAppService.logout(userId);
        return ApiResponse.getSuccess();
    }

    @GetMapping("/drinks")
    public ApiResponse<List<GetDrinksResponse>> getDrinks(@ApiIgnore @RequestAttribute("userId") Long userId) {
        return ApiResponse.getSuccess(userServiceV1.getDrinks(userId));
    }
}
