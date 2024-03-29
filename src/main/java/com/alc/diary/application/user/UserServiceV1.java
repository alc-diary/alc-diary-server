package com.alc.diary.application.user;

import com.alc.diary.application.message.MessageService;
import com.alc.diary.application.user.dto.request.*;
import com.alc.diary.application.user.dto.response.GetDrinksResponse;
import com.alc.diary.application.user.dto.response.GetNotificationSettingAppResponse;
import com.alc.diary.application.user.dto.response.GetUserInfoAppResponse;
import com.alc.diary.application.user.dto.response.SearchUserAppResponse;
import com.alc.diary.domain.drink.Drink;
import com.alc.diary.domain.drink.repository.DrinkRepository;
import com.alc.diary.domain.drinkcategory.DrinkCategory;
import com.alc.diary.domain.drinkcategory.DrinkCategoryRepository;
import com.alc.diary.domain.nickname.BannedWord;
import com.alc.diary.domain.nickname.NicknameBlackListRepository;
import com.alc.diary.domain.exception.DomainException;
import com.alc.diary.domain.user.NotificationSetting;
import com.alc.diary.domain.user.User;
import com.alc.diary.domain.user.UserWithdrawal;
import com.alc.diary.domain.user.error.NotificationSettingError;
import com.alc.diary.domain.user.error.UserError;
import com.alc.diary.domain.user.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class UserServiceV1 {

    private final UserRepository userRepository;
    private final UserDetailRepository userDetailRepository;
    private final UserWithdrawalRepository userWithdrawalRepository;
    private final MessageService messageService;
    private final NicknameBlackListRepository nicknameBlackListRepository;
    private final NotificationSettingRepository notificationSettingRepository;
    private final DrinkCategoryRepository drinkCategoryRepository;
    private final DrinkRepository drinkRepository;

    /**
     * 유저 검색 (현재는 nickname만)
     *
     * @param nickname
     * @return SearchUserAppResponse
     */
    public SearchUserAppResponse searchUser(String nickname) {
        Optional<User> optionalUser = userRepository.findActiveUserByNickname(nickname);
        return optionalUser.map(SearchUserAppResponse::from).orElse(null);
    }

    public UserPublicDto getUserById(Long userId) {
        return userRepository.findById(userId)
                .filter(User::isActive)
                .map(UserPublicDto::fromDomainModel)
                .orElseThrow(() -> new DomainException(UserError.USER_NOT_FOUND, "User ID: " + userId));
    }

    public List<UserPublicDto> getUsersByIds(List<Long> userIds) {
        return userRepository.findByIdIn(userIds).stream()
                .filter(User::isActive)
                .map(UserPublicDto::fromDomainModel)
                .toList();
    }

    /**
     * userId로 유저 정보 조회
     *
     * @param userId
     * @return GetUserInfoAppResponse
     */
    public GetUserInfoAppResponse getUserInfo(Long userId) {
        User foundUser = fetchUserById(userId);
        return GetUserInfoAppResponse.from(foundUser);
    }

    /**
     * 프로필 이미지 변경
     *
     * @param userId
     * @param request
     */
    @Transactional
    public void updateUserProfileImage(Long userId, UpdateUserProfileImageAppRequest request) {
        User foundUser = fetchUserById(userId);
        foundUser.updateProfileImage(request.newProfileImage());
    }

    /**
     * 주량 변경
     *
     * @param userId
     * @param request
     */
    @Transactional
    public void updateAlcoholLimitAndGoal(Long userId, UpdateAlcoholLimitAndGoalAppRequest request) {
        User foundUser = fetchUserById(userId);
        foundUser.updateAlcoholLimitAndGoal(
                request.newPersonalAlcoholLimit(),
                request.newNonAlcoholGoal(),
                request.newAlcoholType()
        );
    }

    /**
     * 닉네임 변경
     *
     * @param userId
     * @param request
     */
    @Transactional
    public void updateNickname(Long userId, UpdateNicknameAppRequest request) {
        User foundUser = fetchUserById(userId);
        List<BannedWord> blackList = nicknameBlackListRepository.findAll();
        for (BannedWord bannedWord : blackList) {
            if (request.newNickname().contains(bannedWord.getWord())) {
                throw new DomainException(UserError.NICKNAME_CONTAINS_BAD_WORD, "request: " + request.newNickname());
            }
        }
        if (userDetailRepository.existsByNickname(request.newNickname())) {
            throw new DomainException(UserError.NICKNAME_ALREADY_TAKEN, "Nickname: " + request.newNickname());
        }
        foundUser.getDetail().updateNickname(request.newNickname());
    }

    /**
     * 코알리 설명 타입 변경
     *
     * @param userId
     * @param request
     */
    @Transactional
    public void updateDescriptionStyle(Long userId, UpdateDescriptionStyleAppRequest request) {
        User foundUser = fetchUserById(userId);
        foundUser.getDetail().updateDescriptionStyle(request.newDescriptionStyle());
    }

    /**
     * 회원 탈퇴 (Soft delete)
     *
     * @param requesterId
     * @param request
     */
    @Transactional
    public void deactivateUser(Long requesterId, DeactivateUserAppRequest request) {
        User targetUser = fetchUserById(request.targetUserId());
        String nickname = targetUser.getNickname();

        targetUser.deactivate(createDeactivateNickname());

        userWithdrawalRepository.save(UserWithdrawal.of(targetUser, request.reason()));

        messageService.send("#알림", nickname + "님이 탈퇴했습니다."); // TODO: 추후에 서비스 분리해서 트랜잭션 분리 예정
    }

    private String createDeactivateNickname() {
        return "탈퇴한유저" + RandomStringUtils.randomAlphanumeric(8);
    }

    private User fetchUserById(Long userId) {
        return userRepository.findActiveUserById(userId)
                .orElseThrow(() -> new DomainException(UserError.USER_NOT_FOUND, "User ID: " + userId));
    }

    /**
     * 푸시 알림 활성화 여부 조회
     *
     * @return
     */
    public GetNotificationSettingAppResponse getNotificationSetting(Long userId) {
        NotificationSetting notificationSetting = fetchNotificationSetting(userId);
        return new GetNotificationSettingAppResponse(notificationSetting.getNotificationEnabled());
    }

    /**
     * 푸시 알림 활성화
     *
     * @param userId
     */
    @Transactional
    public void enableNotificationSetting(Long userId) {
        NotificationSetting notificationSetting = fetchNotificationSetting(userId);
        notificationSetting.enableNotification();
    }

    /**
     * 푸시 알림 비활성화
     *
     * @param userId
     */
    @Transactional
    public void disableNotificationSetting(Long userId) {
        NotificationSetting notificationSetting = fetchNotificationSetting(userId);
        notificationSetting.disableNotification();
    }

    private NotificationSetting fetchNotificationSetting(Long userId) {
        return notificationSettingRepository.findByUserId(userId)
                .orElseThrow(() -> new DomainException(NotificationSettingError.ENTITY_NOT_FOUND));
    }

    public List<GetDrinksResponse> getDrinks(long userId) {
        List<DrinkCategory> drinkCategories = drinkCategoryRepository.findAll();
        List<Drink> drinks = drinkRepository.findAll();

        return GetDrinksResponse.of(drinkCategories, drinks);
    }
}
