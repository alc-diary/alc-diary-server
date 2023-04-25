package com.alc.diary.application.onboarding;

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

@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class OnboardingAppService {

    private final UserRepository userRepository;

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
