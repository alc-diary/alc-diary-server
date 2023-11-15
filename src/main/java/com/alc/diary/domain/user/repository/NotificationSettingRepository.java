package com.alc.diary.domain.user.repository;

import com.alc.diary.domain.user.NotificationSetting;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationSettingRepository extends JpaRepository<NotificationSetting, Long> {
}
