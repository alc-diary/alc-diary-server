package com.alc.diary.application.notification;

import com.alc.diary.application.notification.dto.SaveFcmTokenRequest;
import com.alc.diary.domain.notification.FcmToken;
import com.alc.diary.domain.notification.FcmTokenRepository;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
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

    public void sendFcm(long userId) {
        FcmToken fcmToken = fcmTokenRepository.findByUserId(userId).orElseThrow(() -> new IllegalArgumentException("Token Not Found"));
        Message message = Message.builder()
                .putData("title", "test title")
                .putData("body", "test body")
                .setToken(fcmToken.getToken())
                .build();
        try {
            FirebaseMessaging.getInstance().send(message);
        } catch (FirebaseMessagingException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
