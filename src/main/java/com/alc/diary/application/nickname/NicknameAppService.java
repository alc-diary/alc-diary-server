package com.alc.diary.application.nickname;

import com.alc.diary.application.user.dto.request.CreateRandomNicknameTokenAppRequest;
import com.alc.diary.application.user.dto.response.GetRandomNicknameAppResponse;
import com.alc.diary.application.user.dto.response.GetRandomNicknameTokens;
import com.alc.diary.domain.exception.DomainException;
import com.alc.diary.domain.user.NicknameToken;
import com.alc.diary.domain.user.enums.NicknameTokenOrdinal;
import com.alc.diary.domain.user.error.UserError;
import com.alc.diary.domain.user.repository.NicknameTokenRepository;
import com.alc.diary.domain.user.repository.UserDetailRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Random;

@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class NicknameAppService {

    private final UserDetailRepository userDetailRepository;
    private final NicknameTokenRepository nicknameTokenRepository;

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

    @Transactional
    public void deleteNicknameToken(Long tokenId) {
        nicknameTokenRepository.deleteById(tokenId);
    }

    public GetRandomNicknameAppResponse getRandomNickname() {
        String firstToken = getRandomToken(NicknameTokenOrdinal.FIRST);
        String secondToken = getRandomToken(NicknameTokenOrdinal.SECOND);
        String randomNickname = firstToken + secondToken;
        Random random = new Random();
        for (int i = 0; i < 20; i++) {
            int randomNumber = random.nextInt(10000);
            if (userDetailRepository.findByNickname(randomNickname + randomNumber).isEmpty()) {
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
}
