package com.alc.diary.application.user;

import com.alc.diary.application.user.dto.response.GetUserInfoAppResponse;
import com.alc.diary.domain.exception.DomainException;
import com.alc.diary.domain.user.User;
import com.alc.diary.domain.user.error.UserError;
import com.alc.diary.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserAppService {

    private final UserRepository userRepository;

    public GetUserInfoAppResponse getUser(Long userId) {
        User findUser = userRepository.findById(userId).orElseThrow(() -> new DomainException(UserError.NO_ENTITY_FOUND));
        return new GetUserInfoAppResponse(
            findUser.getDescriptionStyle(),
            findUser.getNickname(),
            "alcoholType",
            findUser.getDrinkAmount(),
            findUser.getNonAlcoholGoal()
        );
    }
}
