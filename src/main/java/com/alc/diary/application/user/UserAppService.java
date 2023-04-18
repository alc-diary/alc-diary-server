package com.alc.diary.application.user;

import com.alc.diary.application.user.dto.request.CreateRandomNicknameTokenAppRequest;
import com.alc.diary.application.user.dto.response.GetRandomNicknameAppResponse;
import com.alc.diary.application.user.dto.response.GetRandomNicknameTokens;
import com.alc.diary.application.user.dto.response.GetUserInfoAppResponse;
import com.alc.diary.application.user.strategy.NicknameStrategy;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class UserAppService {

    private final UserRepository userRepository;
    private final NicknameTokenRepository nicknameTokenRepository;
    private final NicknameStrategy nicknameStrategy;

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

    @Transactional
    public void createRandomNicknameToken(CreateRandomNicknameTokenAppRequest request) {
        if (!StringUtils.hasText(request.nicknameToken())) {
            throw new DomainException(UserError.INVALID_PARAMETER_INCLUDE);
        }
        NicknameToken nicknameToken = new NicknameToken(request.ordinal(), request.nicknameToken());
        try {
            nicknameTokenRepository.save(nicknameToken);
        } catch (DataIntegrityViolationException e) {
            log.error("Duplicate nickname token", e);
            throw new DomainException(UserError.DUPLICATE_NICKNAME_TOKEN);
        }
    }

    public GetRandomNicknameTokens getRandomNicknameTokens() {
        List<GetRandomNicknameTokens.NicknameTokenDto> firstNicknameTokenDtos = nicknameTokenRepository.findByOrdinal(NicknameTokenOrdinal.FIRST).stream()
                .map(nicknameToken -> new GetRandomNicknameTokens.NicknameTokenDto(nicknameToken.getId(), nicknameToken.getToken()))
                .collect(Collectors.toList());
        List<GetRandomNicknameTokens.NicknameTokenDto> secondNicknameTokenDtos = nicknameTokenRepository.findByOrdinal(NicknameTokenOrdinal.SECOND).stream()
                .map(nicknameToken -> new GetRandomNicknameTokens.NicknameTokenDto(nicknameToken.getId(), nicknameToken.getToken()))
                .collect(Collectors.toList());
        return new GetRandomNicknameTokens(firstNicknameTokenDtos, secondNicknameTokenDtos);
    }

    public GetRandomNicknameAppResponse getRandomNickname() {
        NicknameToken firstToken = nicknameTokenRepository.findByOrdinalOrderByRandLimit1(NicknameTokenOrdinal.FIRST)
            .orElseThrow(RuntimeException::new);
        NicknameToken secondToken = nicknameTokenRepository.findByOrdinalOrderByRandLimit1(NicknameTokenOrdinal.SECOND)
            .orElseThrow(RuntimeException::new);
        String randomNickname = firstToken + " " + secondToken;
        for (int i = 0; i < 20; i++) {
            int randomNumber = new Random().nextInt(10000);
            if (!userRepository.findByNickname(randomNickname + randomNumber).isPresent()) {
                return new GetRandomNicknameAppResponse(randomNickname + randomNickname);
            }
        }
        throw new IllegalArgumentException();
    }

    @Transactional
    public void deleteNicknameToken(Long tokenId) {
        nicknameTokenRepository.deleteById(tokenId);
    }
}
