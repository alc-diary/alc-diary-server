package com.alc.diary.application.onboarding;

import com.alc.diary.application.message.MessageService;
import com.alc.diary.application.onboarding.dto.response.GetIsOnboardingDoneAppResponse;
import com.alc.diary.application.user.dto.request.UpdateUserOnboardingInfoAppRequest;
import com.alc.diary.application.user.dto.response.CheckNicknameAvailableAppResponse;
import com.alc.diary.domain.exception.DomainException;
import com.alc.diary.domain.user.User;
import com.alc.diary.domain.user.UserDetail;
import com.alc.diary.domain.user.UserHistory;
import com.alc.diary.domain.user.error.UserError;
import com.alc.diary.domain.user.repository.UserDetailRepository;
import com.alc.diary.domain.user.repository.UserHistoryRepository;
import com.alc.diary.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.alc.diary.domain.user.enums.UserStatus.ACTIVE;

@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class OnboardingAppService {

    private final UserRepository userRepository;
    private final UserDetailRepository userDetailRepository;
    private final UserHistoryRepository userHistoryRepository;
    private final MessageService messageService;

    public CheckNicknameAvailableAppResponse checkNicknameAvailable(String nickname) {
        if (userDetailRepository.findByNickname(nickname).isPresent()) {
            return new CheckNicknameAvailableAppResponse(false);
        }
        return new CheckNicknameAvailableAppResponse(true);
    }

    @Transactional
    public void updateUserOnboardingInfo(Long userId, UpdateUserOnboardingInfoAppRequest request) {
        User findUser = userRepository.findByIdAndStatusEqualsOnboarding(userId)
                                      .orElseThrow(() -> new DomainException(UserError.USER_NOT_FOUND));
        if (!findUser.isOnboarding()) {
            throw new DomainException(UserError.NOT_IN_ONBOARDING_PROCESS);
        }
        if (userDetailRepository.findByNickname(request.nickname()).isPresent()) {
            throw new DomainException(UserError.NICKNAME_ALREADY_TAKEN);
        }
        UserDetail userDetail = new UserDetail(
                null,
                findUser,
                request.nickname(),
                request.alcoholType(),
                request.personalAlcoholLimit(),
                request.nonAlcoholGoal(),
                request.descriptionStyle()
        );
        userDetailRepository.save(userDetail);
        findUser.onboarding(userDetail);
        createHistory(findUser.getId(), findUser);
        try {
            messageService.send("#알림", findUser.getDetail().getNickname() + "님 온보딩 완료!");
        } catch (Exception e) {
            log.error("메시지 전송에 실패했습니다.");
        }
    }

    private void createHistory(Long requesterId, User targetUser) {
        UserHistory history = UserHistory.from(requesterId, targetUser);
        userHistoryRepository.save(history);
    }
}
