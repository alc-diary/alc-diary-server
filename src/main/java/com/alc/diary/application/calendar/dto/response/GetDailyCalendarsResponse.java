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
import java.util.function.Predicate;
import java.util.stream.Stream;

public record GetDailyCalendarsResponse(

        long calendarId,
        UserDto owner,
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
                .map(calendar -> {
                    User owner = userById.get(calendar.getOwnerId());
                    return new GetDailyCalendarsResponse(
                            calendar.getId(),
                            UserDto.fromDomainModel(owner),
                            calendar.getTitle(),
                            calendar.getDrinkStartTime().toString(),
                            calendar.getDrinkEndTime().toString(),
                            calendar.findUserCalendarByUserId(userId).map(UserCalendar::getDrinkingRecorded).orElse(false),
                            calendar.findUserCalendarByUserId(userId)
                                    .map(userCalendar -> userCalendar.getDrinkRecords().stream()
                                            .map(drinkRecord -> new DrinkRecordDto(
                                                    drinkRecord.getType(),
                                                    drinkRecord.getUnit()
                                            )))
                                    .orElseGet(Stream::empty)
                                    .toList(),
                            calendar.findUserCalendarByUserId(userId)
                                    .map(userCalendar -> userCalendar.getDrinkRecords().stream()
                                            .filter(drinkRecord -> !drinkRecord.isDeleted())
                                            .mapToDouble(DrinkRecord::getQuantity)
                                            .sum())
                                    .orElse(0.0)
                                    .floatValue(),
                            calendar.findUserCalendarsExcludingUserId(owner.getId()).stream()
                                    .map(UserCalendar::getUserId)
                                    .filter(id -> userById.get(id) != null)
                                    .map(userById::get)
                                    .map(user -> new UserDto(
                                            user.getId(),
                                            user.getNickname(),
                                            user.getProfileImage()
                                    ))
                                    .toList()
                    );
                })
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
            String nickname,
            String profileImageUrl
    ) {

        public static UserDto fromDomainModel(User user) {
            return new UserDto(
                    user != null ? user.getId() : 0,
                    user != null ? user.getNickname() : "탈퇴한 유저",
                    user != null ? user.getProfileImage() : null
            );
        }
    }
}
