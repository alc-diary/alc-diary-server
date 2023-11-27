package com.alc.diary.application.notification;

import com.alc.diary.application.notification.dto.SaveFcmTokenRequest;
import com.alc.diary.domain.exception.DomainException;
import com.alc.diary.domain.notification.FcmToken;
import com.alc.diary.domain.notification.FcmTokenRepository;
import com.alc.diary.domain.user.NotificationSetting;
import com.alc.diary.domain.user.User;
import com.alc.diary.domain.user.error.NotificationSettingError;
import com.alc.diary.domain.user.error.UserError;
import com.alc.diary.domain.user.repository.NotificationSettingRepository;
import com.alc.diary.domain.user.repository.UserRepository;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class NotificationService {

    private final FcmTokenRepository fcmTokenRepository;
    private final UserRepository userRepository;
    private final NotificationSettingRepository notificationSettingRepository;

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

    @Async
    public void sendFcm(long userId, String title, String body, String eventName) {
        NotificationSetting notificationSetting = notificationSettingRepository.findByUserId(userId)
                .orElseThrow(() -> new DomainException(NotificationSettingError.ENTITY_NOT_FOUND));
        if (!notificationSetting.getNotificationEnabled()) {
            return;
        }

        FcmToken fcmToken = fcmTokenRepository.findByUserId(userId).orElse(null);
        if (fcmToken == null) {
            return;
        }
        User user = userRepository.findActiveUserById(userId).orElseThrow(() -> new DomainException(UserError.USER_NOT_FOUND));
        Notification notification = Notification.builder()
                .setTitle(title)
                .setBody(body)
                .setImage(user.getProfileImage())
                .build();
        Message message = Message.builder()
                .setNotification(notification)
                .putData("event", eventName)
                .setToken(fcmToken.getToken())
                .build();
        try {
            FirebaseMessaging.getInstance().send(message);
        } catch (Exception e) {
            log.error("Failed to send FCM message", e);
        }
    }
}
