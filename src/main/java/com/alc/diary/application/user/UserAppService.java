package com.alc.diary.application.user;

import com.alc.diary.application.user.dto.request.*;
import com.alc.diary.application.user.dto.response.GetUserInfoAppResponse;
import com.alc.diary.application.user.dto.response.SearchUserAppResponse;
import com.alc.diary.domain.exception.DomainException;
import com.alc.diary.domain.user.User;
import com.alc.diary.domain.user.UserWithdrawal;
import com.alc.diary.domain.user.error.UserError;
import com.alc.diary.domain.user.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class UserAppService {

    private final UserRepository userRepository;
    private final UserDetailRepository userDetailRepository;
    private final UserWithdrawalRepository userWithdrawalRepository;

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

    /**
     * userId로 유저 정보 조회
     *
     * @param userId
     * @return GetUserInfoAppResponse
     */
    public GetUserInfoAppResponse getUserInfo(Long userId) {
        User foundUser = getUserById(userId);
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
        User foundUser = getUserById(userId);
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
        User foundUser = getUserById(userId);
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
        User foundUser = getUserById(userId);
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
        User foundUser = getUserById(userId);
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
        User targetUser = getUserById(request.targetUserId());
        targetUser.delete();
        userWithdrawalRepository.save(UserWithdrawal.of(targetUser, request.reason()));
    }

    private User getUserById(Long userId) {
        return userRepository.findActiveUserById(userId)
                             .orElseThrow(() -> new DomainException(UserError.USER_NOT_FOUND, "User ID: " + userId));
    }
}
