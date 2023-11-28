package com.alc.diary.application.user;

import com.alc.diary.domain.notification.FcmTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class LogoutAppService {

    private final FcmTokenRepository fcmTokenRepository;

    @Transactional
    public void logout(Long userId) {
        fcmTokenRepository.deleteByUserId(userId);
    }
}
