package com.alc.diary.application.calendar.dto.request;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class SearchCalendarAppRequest {

    private final String query;
    private final Integer year;
    private final Integer month;
    private final Integer day;

    public SearchCalendarAppRequest(String query, Integer year, Integer month, Integer day) {
        this.query = query;
        this.year = year;
        this.month = month;
        this.day = day;
    }

    public LocalDateTime getStartTime() {
        if (query.equals("DAILY")) {
            return LocalDate.of(year, month, day).atStartOfDay();
        }
        if (query.equals("MONTHLY")) {
            return LocalDate.of(year, month, 1).atStartOfDay();
        }
        throw new RuntimeException();
    }

    public LocalDateTime getEndTime() {
        if (query.equals("DAILY")) {
            return LocalDate.of(year, month, day).plusDays(1).atStartOfDay();
        }

        if (query.equals("MONTHLY")) {
            return LocalDate.of(year, month, 1).plusMonths(1).atStartOfDay();
        }
        throw new RuntimeException();
    }
}
