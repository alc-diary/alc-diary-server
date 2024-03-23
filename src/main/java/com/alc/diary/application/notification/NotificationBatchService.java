package com.alc.diary.application.notification;

import com.alc.diary.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class NotificationBatchService {

    private final UserRepository userRepository;
    private final NotificationService notificationService;

    @Scheduled(cron = "0 0 9 * * MON,WED")
    public void batchTest() {
        List<Long> userIds = userRepository.findNotificationEnabledUserIdsWithToken();

        batchSendPush(
                userIds,
                20,
                "술렁술렁",
                "안녕! 좋은 아침이야 :)\n혹시 어제 술 마시지 않았니? 얼마나 마셨어?",
                "TEMPORARY");
    }

    @Scheduled(cron = "0 0 11 * * SAT")
    public void saturdayNotification() {
        List<Long> userIds = userRepository.findNotificationEnabledUserIdsWithToken();

        batchSendPush(
                userIds,
                20,
                "술렁술렁",
                "안녕! 좋은 주말이야!\n혹시 어제 불금 보냈니? 속은 괜찮아?",
                "TEMPORARY");
    }

    private void batchSendPush(List<Long> userIds, int batchSize, String title, String body, String eventName) {
        for (int i = 0; i < userIds.size(); i += batchSize) {
            List<Long> subUserIds = userIds.subList(i, Math.min(userIds.size(), i + batchSize));
            subUserIds.forEach(userId -> attemptSendFcm(userId, title, body, eventName));
        }
    }

    private void attemptSendFcm(long userId, String title, String body, String eventName) {
        final int maxAttempts = 3;
        int attempt = 0;
        while (attempt < maxAttempts) {
            try {
                notificationService.sendFcm(userId, title, body, eventName);
                break;
            } catch (Exception e) {
                attempt++;
                if (attempt >= maxAttempts) {
                    log.error("Failed to send FCM after " + maxAttempts + " attempts", e);
                } else {
                    try {
                        Thread.sleep(2_000L * attempt);
                    } catch (InterruptedException ex) {
                        Thread.currentThread().interrupt();
                        log.error("Notification send retry interrupted", ex);
                    }
                }
            }
        }
    }
}
