package com.alc.diary.application.notification;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class NotificationBatchService {

    private final NotificationService notificationService;

    @Scheduled(cron = "0 * 15 * * MON, WED")
    public void batchTest() {
        notificationService.sendFcm(120, "zz", "테스트", "TEST");
    }
}
