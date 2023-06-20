package com.alc.diary.application.user;

import com.alc.diary.application.user.dto.request.*;
import com.alc.diary.application.user.dto.response.GetUserInfoAppResponse;
import com.alc.diary.domain.exception.DomainException;
import com.alc.diary.domain.user.User;
import com.alc.diary.domain.user.UserWithdrawal;
import com.alc.diary.domain.user.error.UserError;
import com.alc.diary.domain.user.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class UserAppService {

    private final UserRepository userRepository;
    private final UserDetailRepository userDetailRepository;
    private final UserWithdrawalRepository userWithdrawalRepository;

    public GetUserInfoAppResponse getUserInfo(Long userId) {
        User foundUser = getUserById(userId);
        return new GetUserInfoAppResponse(
                foundUser.getId(),
                foundUser.getDetail().getDescriptionStyle(),
                foundUser.getDetail().getAlcoholType(),
                foundUser.getDetail().getNickname(),
                foundUser.getDetail().getPersonalAlcoholLimit(),
                foundUser.getDetail().getNonAlcoholGoal(),
                foundUser.getProfileImage(),
                foundUser.getStatus()
        );
    }

    @Transactional
    public void updateUserProfileImage(Long userId, UpdateUserProfileImageAppRequest request) {
        User foundUser = getUserById(userId);
        foundUser.updateProfileImage(request.newProfileImage());
    }

    @Transactional
    public void updateAlcoholLimitAndGoal(Long userId, UpdateAlcoholLimitAndGoalAppRequest request) {
        User foundUser = getUserById(userId);
        foundUser.updateAlcoholLimitAndGoal(
                request.newPersonalAlcoholLimit(),
                request.newNonAlcoholGoal(),
                request.newAlcoholType()
        );
    }

    @Transactional
    public void updateNickname(Long userId, UpdateNicknameAppRequest request) {
        User foundUser = getUserById(userId);
        if (userDetailRepository.existsByNickname(request.newNickname())) {
            throw new DomainException(UserError.NICKNAME_ALREADY_TAKEN, "Nickname: " + request.newNickname());
        }
        foundUser.getDetail().updateNickname(request.newNickname());
    }

    @Transactional
    public void updateDescriptionStyle(Long userId, UpdateDescriptionStyleAppRequest request) {
        User foundUser = getUserById(userId);
        foundUser.getDetail().updateDescriptionStyle(request.newDescriptionStyle());
    }

    @Transactional
    public void deactivateUser(Long requesterId, DeactivateUserAppRequest request) {
        User targetUser = getUserById(request.targetUserId());
        targetUser.delete();
        userWithdrawalRepository.save(UserWithdrawal.of(targetUser, request.reason()));
    }

    private User getUserById(Long userId) {
        return userRepository.findById(userId)
                             .orElseThrow(() -> new DomainException(UserError.USER_NOT_FOUND, "User ID: " + userId));
    }
}
