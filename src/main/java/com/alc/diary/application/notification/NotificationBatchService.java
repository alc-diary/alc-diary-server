package com.alc.diary.application.notification;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class NotificationBatchService {

    private final NotificationService notificationService;

    @Scheduled(cron = "0 * 0 * * MON,TUE,WED")
    public void batchTest() {
        notificationService.sendFcm(120, "술렁술렁", "안녕! 좋은 아침이야 :)\n혹시 어제 술 마시지 않았니? 얼마나 마셨어?", "TEMPORARY");
    }
}
