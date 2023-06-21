package com.alc.diary.application.user;

import com.alc.diary.application.onboarding.dto.response.GetIsOnboardingDoneAppResponse;
import com.alc.diary.domain.exception.DomainException;
import com.alc.diary.domain.user.error.UserError;
import com.alc.diary.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.alc.diary.domain.user.enums.UserStatus.ACTIVE;

@RequiredArgsConstructor
@Service
public class UserStatusAppService {

    private final UserRepository userRepository;

    public GetIsOnboardingDoneAppResponse getIsOnboardingDone(long userId) {
        return new GetIsOnboardingDoneAppResponse(
                userRepository.findNotDeactivatedUserById(userId)
                              .map(user -> user.getStatus() == ACTIVE)
                              .orElseThrow(() -> new DomainException(UserError.USER_NOT_FOUND, "User ID: " + userId))
        );
    }
}
