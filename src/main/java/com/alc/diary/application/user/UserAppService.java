package com.alc.diary.application.user;

import com.alc.diary.application.user.dto.request.*;
import com.alc.diary.application.user.dto.response.GetRandomNicknameAppResponse;
import com.alc.diary.application.user.dto.response.GetRandomNicknameTokens;
import com.alc.diary.application.user.dto.response.GetUserInfoAppResponse;
import com.alc.diary.domain.exception.DomainException;
import com.alc.diary.domain.user.NicknameToken;
import com.alc.diary.domain.user.User;
import com.alc.diary.domain.user.UserHistory;
import com.alc.diary.domain.user.UserWithdrawal;
import com.alc.diary.domain.user.enums.NicknameTokenOrdinal;
import com.alc.diary.domain.user.error.UserError;
import com.alc.diary.domain.user.repository.*;
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
public class UserAppService {

    private final UserRepository userRepository;
    private final UserDetailRepository userDetailRepository;
    private final UserWithdrawalRepository userWithdrawalRepository;
    private final UserHistoryRepository userHistoryRepository;
    private final NicknameTokenRepository nicknameTokenRepository;

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

    @Transactional
    public void deleteNicknameToken(Long tokenId) {
        nicknameTokenRepository.deleteById(tokenId);
    }

    @Transactional
    public void updateUserProfileImage(Long userId, UpdateUserProfileImageAppRequest request) {
        User foundUser = getUserById(userId);
        foundUser.updateProfileImage(request.newProfileImage());
        createHistory(foundUser.getId(), foundUser);
    }

    @Transactional
    public void updateAlcoholLimitAndGoal(Long userId, UpdateAlcoholLimitAndGoalAppRequest request) {
        User foundUser = getUserById(userId);
        foundUser.updateAlcoholLimitAndGoal(
                request.newPersonalAlcoholLimit(),
                request.newNonAlcoholGoal(),
                request.newAlcoholType()
        );
        createHistory(foundUser.getId(), foundUser);
    }

    @Transactional
    public void updateNickname(Long userId, UpdateNicknameAppRequest request) {
        User foundUser = getUserById(userId);
        if (userDetailRepository.existsByNickname(request.newNickname())) {
            throw new DomainException(UserError.NICKNAME_ALREADY_TAKEN);
        }
        foundUser.getDetail().updateNickname(request.newNickname());
        createHistory(foundUser.getId(), foundUser);
    }

    @Transactional
    public void updateDescriptionStyle(Long userId, UpdateDescriptionStyleAppRequest request) {
        User foundUser = getUserById(userId);
        foundUser.getDetail().updateDescriptionStyle(request.newDescriptionStyle());
        createHistory(foundUser.getId(), foundUser);
    }

    @Transactional
    public void deactivateUser(Long requesterId, DeactivateUserAppRequest request) {
        User targetUser = getUserById(request.targetUserId());
        targetUser.delete();
        userWithdrawalRepository.save(UserWithdrawal.of(targetUser, request.reason()));
        createHistory(requesterId, targetUser);
    }

    private User getUserById(Long userId) {
        return userRepository.findById(userId)
                             .orElseThrow(() -> new DomainException(UserError.USER_NOT_FOUND));
    }

    private void createHistory(Long requesterId, User targetUser) {
        UserHistory history = UserHistory.from(requesterId, targetUser);
        userHistoryRepository.save(history);
    }
}
