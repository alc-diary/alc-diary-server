package com.alc.diary.application.onboarding;

import com.alc.diary.application.message.MessageService;
import com.alc.diary.application.user.dto.request.UpdateUserOnboardingInfoAppRequest;
import com.alc.diary.application.user.dto.response.CheckNicknameAvailableAppResponse;
import com.alc.diary.domain.nickname.BannedWord;
import com.alc.diary.domain.nickname.NicknameBlackListRepository;
import com.alc.diary.domain.exception.DomainException;
import com.alc.diary.domain.user.User;
import com.alc.diary.domain.user.UserDetail;
import com.alc.diary.domain.user.error.UserError;
import com.alc.diary.domain.user.repository.UserDetailRepository;
import com.alc.diary.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class OnboardingAppService {

    private final UserRepository userRepository;
    private final UserDetailRepository userDetailRepository;
    private final MessageService messageService;
    private final NicknameBlackListRepository nicknameBlackListRepository;

    public CheckNicknameAvailableAppResponse checkNicknameAvailable(String nickname) {
        if (userDetailRepository.findByNickname(nickname).isPresent()) {
            return new CheckNicknameAvailableAppResponse(false);
        }
        return new CheckNicknameAvailableAppResponse(true);
    }

    @Transactional
    public void updateUserOnboardingInfo(Long userId, UpdateUserOnboardingInfoAppRequest request) {
        User findUser = userRepository.findOnboardingUserById(userId)
                                      .orElseThrow(() -> new DomainException(UserError.USER_NOT_FOUND));
        if (!findUser.isOnboarding()) {
            throw new DomainException(UserError.NOT_IN_ONBOARDING_PROCESS);
        }
        if (userDetailRepository.findByNickname(request.nickname()).isPresent()) {
            throw new DomainException(UserError.NICKNAME_ALREADY_TAKEN);
        }
        List<BannedWord> blackList = nicknameBlackListRepository.findAll();
        for (BannedWord bannedWord : blackList) {
            if (request.nickname().contains(bannedWord.getWord())) {
                throw new DomainException(UserError.NICKNAME_CONTAINS_BAD_WORD, "request: " + request.nickname());
            }
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
        try {
            messageService.send("#알림", findUser.getDetail().getNickname() + "님 온보딩 완료!");
        } catch (Exception e) {
            log.error("메시지 전송에 실패했습니다.");
        }
    }
}
