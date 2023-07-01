package com.alc.diary.application.calendar.dto.response;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public record GetMonthlyCalendarsResponse(

        String date,
        long drinkUnitInfoId
) implements Comparable<GetMonthlyCalendarsResponse> {

    @Override
    public boolean equals(Object obj) {
        GetMonthlyCalendarsResponse other = (GetMonthlyCalendarsResponse) obj;
        return date.equals(other.date);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(date);
    }

    @Override
    public int compareTo(@NotNull GetMonthlyCalendarsResponse o) {
        return date.compareTo(o.date);
    }
}
