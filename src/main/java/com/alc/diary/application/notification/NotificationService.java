package com.alc.diary.application.notification;

import com.alc.diary.application.notification.dto.SaveFcmTokenRequest;
import com.alc.diary.domain.notification.FcmToken;
import com.alc.diary.domain.notification.FcmTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class NotificationService {

    private final FcmTokenRepository fcmTokenRepository;

    @Transactional
    public void saveFcmToken(long userId, SaveFcmTokenRequest request) {
        fcmTokenRepository.findByUserId(userId)
                .ifPresentOrElse(
                        fcmToken -> fcmToken.updateToken(request.token()),
                        () -> {
                            FcmToken fcmTokenToSave = FcmToken.create(userId, request.token());
                            fcmTokenRepository.save(fcmTokenToSave);
                        }
                );
    }

    public void sendFcm() {

    }
}
