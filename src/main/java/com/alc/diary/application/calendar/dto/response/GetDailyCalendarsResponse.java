package com.alc.diary.application.calendar.dto.response;

import com.alc.diary.domain.calendar.Calendar;
import com.alc.diary.domain.calendar.DrinkRecord;
import com.alc.diary.domain.calendar.UserCalendar;
import com.alc.diary.domain.calendar.enums.DrinkType;
import com.alc.diary.domain.calendar.enums.DrinkUnit;
import com.alc.diary.domain.user.User;
import io.swagger.annotations.ApiModel;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public record GetDailyCalendarsResponse(

        long calendarId,
        String title,
        String drinkStartTime,
        String drinkEndTime,
        boolean drinkingRecorded,
        List<DrinkRecordDto> drinkRecords,
        float currentUserDrinkTotal,
        List<UserDto> taggedUsers
) {

    public static List<GetDailyCalendarsResponse> of(long userId, List<Calendar> calendars, Map<Long, User> userById) {
        return calendars.stream()
                .map(calendar -> new GetDailyCalendarsResponse(
                        calendar.getId(),
                        calendar.getTitle(),
                        calendar.getDrinkStartTime().toString(),
                        calendar.getDrinkEndTime().toString(),
                        calendar.findUserCalendarByUserId(userId).map(UserCalendar::isDrinkingRecorded).orElse(false),
                        calendar.findUserCalendarByUserId(userId)
                                .map(userCalendar -> userCalendar.getDrinkRecords().stream()
                                        .map(drinkRecord -> new GetDailyCalendarsResponse.DrinkRecordDto(
                                                drinkRecord.getType(),
                                                drinkRecord.getUnit()
                                        )))
                                .orElseGet(Stream::empty)
                                .toList(),
                        calendar.findUserCalendarByUserId(userId)
                                .map(userCalendar -> userCalendar.getDrinkRecords().stream()
                                        .mapToDouble(DrinkRecord::getQuantity)
                                        .sum())
                                .orElse(0.0)
                                .floatValue(),
                        calendar.findUserCalendarsExcludingUserId(userId).stream()
                                .map(UserCalendar::getUserId)
                                .map(userById::get)
                                .map(user -> new GetDailyCalendarsResponse.UserDto(
                                        user.getId(),
                                        user.getProfileImage()
                                ))
                                .toList()
                ))
                .toList();
    }

    @ApiModel(value = "GetDailyCalendarsResponse_DrinkRecordDto")
    public record DrinkRecordDto(

            DrinkType drinkType,
            DrinkUnit drinkUnit
    ) {
    }

    @ApiModel(value = "GetDailyCalendarsResponse_UserDto")
    public record UserDto(

            long id,
            String profileImageUrl
    ) {
    }
}
