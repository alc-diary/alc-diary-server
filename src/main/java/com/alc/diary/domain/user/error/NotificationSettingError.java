package com.alc.diary.domain.user.error;

import com.alc.diary.domain.exception.ErrorModel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum NotificationSettingError implements ErrorModel {

    ENTITY_NOT_FOUND("NS_E0000", "Notification setting not found.");

    private final String code;
    private final String message;
}
