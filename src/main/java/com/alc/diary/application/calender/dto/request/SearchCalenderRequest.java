package com.alc.diary.application.calender.dto.request;

import com.alc.diary.domain.calender.enums.QueryType;

public record SearchCalenderRequest(
        long userId,
        QueryType query,
        String date
) {
}
