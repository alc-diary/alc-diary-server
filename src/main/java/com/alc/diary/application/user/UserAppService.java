package com.alc.diary.application.user;

import com.alc.diary.application.user.dto.request.CreateRandomNicknameTokenAppRequest;
import com.alc.diary.application.user.dto.response.GetRandomNicknameAppResponse;
import com.alc.diary.application.user.dto.response.GetUserInfoAppResponse;
import com.alc.diary.domain.exception.DomainException;
import com.alc.diary.domain.user.NicknameToken;
import com.alc.diary.domain.user.User;
import com.alc.diary.domain.user.enums.NicknameTokenOrdinal;
import com.alc.diary.domain.user.error.UserError;
import com.alc.diary.domain.user.repository.NicknameTokenRepository;
import com.alc.diary.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserAppService {

    private final UserRepository userRepository;
    private final NicknameTokenRepository nicknameTokenRepository;

    public GetUserInfoAppResponse getUser(Long userId) {
        User findUser = userRepository.findById(userId).orElseThrow(() -> new DomainException(UserError.USER_NOT_FOUND));
        return new GetUserInfoAppResponse(
            findUser.getId(),
            findUser.getDescriptionStyle(),
            findUser.getStatus(),
            findUser.getAlcoholType(),
            findUser.getNickname(),
            findUser.getDrinkAmount(),
            findUser.getNonAlcoholGoal()
        );
    }

    public void createRandomNicknameToken(CreateRandomNicknameTokenAppRequest request) {
        NicknameToken nicknameToken = new NicknameToken(request.ordinal(), request.nicknameToken());
        try {
            nicknameTokenRepository.save(nicknameToken);
        } catch (DataIntegrityViolationException e) {
            log.error("Duplicate nickname token", e);
            throw new DomainException(UserError.DUPLICATE_NICKNAME_TOKEN);
        }
    }

    public GetRandomNicknameAppResponse getRandomNickname() {
        NicknameToken firstToken = nicknameTokenRepository.findByOrdinalOrderByRandLimit1(NicknameTokenOrdinal.FIRST)
            .orElseThrow(RuntimeException::new);
        NicknameToken secondToken = nicknameTokenRepository.findByOrdinalOrderByRandLimit1(NicknameTokenOrdinal.SECOND)
            .orElseThrow(RuntimeException::new);

        return new GetRandomNicknameAppResponse(firstToken.getToken() + secondToken.getToken());
    }
}
