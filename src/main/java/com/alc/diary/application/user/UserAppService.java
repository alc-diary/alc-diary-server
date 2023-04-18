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
import org.springframework.data.domain.PageRequest;
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
            .toList();
        List<GetRandomNicknameTokens.NicknameTokenDto> secondNicknameTokenDtos = nicknameTokenRepository.findByOrdinal(NicknameTokenOrdinal.SECOND).stream()
            .map(nicknameToken -> new GetRandomNicknameTokens.NicknameTokenDto(nicknameToken.getId(), nicknameToken.getToken()))
            .toList();
        return new GetRandomNicknameTokens(firstNicknameTokenDtos, secondNicknameTokenDtos);
    }

    public GetRandomNicknameAppResponse getRandomNickname() {
        String firstToken = getRandomToken(NicknameTokenOrdinal.FIRST);
        String secondToken = getRandomToken(NicknameTokenOrdinal.SECOND);
        String randomNickname = firstToken + secondToken;
        Random random = new Random();
        for (int i = 0; i < 20; i++) {
            int randomNumber = random.nextInt(10000);
            if (userRepository.findByNickname(randomNickname + randomNumber).isEmpty()) {
                return new GetRandomNicknameAppResponse(randomNickname + randomNumber);
            }
        }
        throw new IllegalArgumentException();
    }

    private String getRandomToken(NicknameTokenOrdinal ordinal) {
        return nicknameTokenRepository.findByOrdinalOrderByRandLimit1(ordinal, PageRequest.of(0, 1)).stream()
            .findFirst()
            .map(NicknameToken::getToken)
            .orElseThrow(RuntimeException::new);
    }

    @Transactional
    public void deleteNicknameToken(Long tokenId) {
        nicknameTokenRepository.deleteById(tokenId);
    }
}
