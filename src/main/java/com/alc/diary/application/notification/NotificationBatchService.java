package com.alc.diary.application.notification;

import com.alc.diary.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class NotificationBatchService {

    private final UserRepository userRepository;
    private final NotificationService notificationService;

    @Scheduled(cron = "0 0 9 * * MON,WED")
    public void batchTest() {
        List<Long> userIds = userRepository.findNotificationEnabledUserIdsWithToken();

        for (long userId : userIds) {
            notificationService.sendFcm(userId, "술렁술렁", "안녕! 좋은 아침이야 :)\n혹시 어제 술 마시지 않았니? 얼마나 마셨어?", "TEMPORARY");
        }
    }

    @Scheduled(cron = "0 0 11 * * SAT")
    public void saturdayNotification() {
        List<Long> userIds = userRepository.findNotificationEnabledUserIdsWithToken();

        for (long userId : userIds) {
            notificationService.sendFcm(userId, "술렁술렁", "안녕! 좋은 주말이야!\n혹시 어제 불금 보냈니? 속은 괜찮아?", "TEMPORARY");
        }
    }
}
