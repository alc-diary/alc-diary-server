package com.alc.diary.application.onboarding;

import com.alc.diary.application.onboarding.dto.response.GetIsOnboardingDoneAppResponse;
import com.alc.diary.application.user.dto.request.UpdateUserOnboardingInfoAppRequest;
import com.alc.diary.application.user.dto.response.CheckNicknameAvailableAppResponse;
import com.alc.diary.domain.exception.DomainException;
import com.alc.diary.domain.user.User;
import com.alc.diary.domain.user.error.UserError;
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

    public GetIsOnboardingDoneAppResponse getIsOnboardingDone(Long userId) {
        return new GetIsOnboardingDoneAppResponse(
                userRepository.findByIdIgnoringWhere(userId)
                              .map(user -> user.getStatus() == ACTIVE)
                              .orElseThrow(() -> new DomainException(UserError.USER_NOT_FOUND))
        );
    }

    public CheckNicknameAvailableAppResponse checkNicknameAvailable(String nickname) {
        if (userRepository.findByNickname(nickname).isPresent()) {
            return new CheckNicknameAvailableAppResponse(false);
        }
        return new CheckNicknameAvailableAppResponse(true);
    }

    @Transactional
    public void updateUserOnboardingInfo(Long userId, UpdateUserOnboardingInfoAppRequest request) {
        User findUser = userRepository.findById(userId)
                                      .orElseThrow(() -> new DomainException(UserError.USER_NOT_FOUND));
        if (userRepository.findByNickname(request.nickname()).isPresent()) {
            throw new DomainException(UserError.NICKNAME_ALREADY_TAKEN);
        }
        findUser.onboarding(
                request.descriptionStyle(),
                request.nickname(),
                request.alcoholType(),
                request.personalAlcoholLimit(),
                request.nonAlcoholGoal()
        );
    }
}
