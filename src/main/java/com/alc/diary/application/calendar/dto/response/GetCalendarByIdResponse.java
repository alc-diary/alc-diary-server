package com.alc.diary.application.calendar.dto.response;

import com.alc.diary.domain.calendar.Calendar;
import com.alc.diary.domain.calendar.Photo;
import com.alc.diary.domain.calendar.enums.DrinkType;
import com.alc.diary.domain.calendar.enums.DrinkUnit;
import com.alc.diary.domain.user.User;
import io.swagger.annotations.ApiModel;

import java.util.List;
import java.util.Map;

public record GetCalendarByIdResponse(

        long calendarId,
        long ownerId,
        String title,
        String drinkStartTime,
        String drinkEndTime,
        List<UserCalendarDto> userCalendars,
        List<PhotoDto> photos
) {

    public static GetCalendarByIdResponse of(long userId, Calendar calendar, Map<Long, User> userByUserId) {
        return new GetCalendarByIdResponse(
                calendar.getId(),
                calendar.getOwnerId(),
                calendar.getTitle(),
                calendar.getDrinkStartTime().toString(),
                calendar.getDrinkEndTime().toString(),
                calendar.getUserCalendars().stream()
                        .map(userCalendar -> new UserCalendarDto(
                                userCalendar.getId(),
                                new UserDto(
                                        userCalendar.getUserId(),
                                        userByUserId.get(userCalendar.getUserId()).getNickname(),
                                        userByUserId.get(userCalendar.getUserId()).getProfileImage()
                                ),
                                userCalendar.getContent(),
                                userCalendar.getDrinkCondition(),
                                userCalendar.getDrinkRecords().stream()
                                        .map(drinkRecord -> new DrinkRecordDto(
                                                drinkRecord.getId(),
                                                drinkRecord.getType(),
                                                drinkRecord.getUnit(),
                                                drinkRecord.getQuantity()
                                        ))
                                        .toList()
                        ))
                        .toList(),
                calendar.getPhotos().stream()
                        // .filter(photo -> !photo.isDeleted())
                        .map(photo -> new PhotoDto(
                                photo.getId(),
                                photo.getUserId(),
                                photo.getUrl()
                        ))
                        .toList()
        );
    }

    @ApiModel(value = "GetCalendarByIdResponse_UserCalendarDto")
    public record UserCalendarDto(

            long id,
            UserDto user,
            String content,
            String drinkCondition,
            List<DrinkRecordDto> drinkRecords
    ) {
    }

    @ApiModel(value = "GetCalendarByIdResponse_UserDto")
    public record UserDto(

            long id,
            String nickname,
            String profileImageUrl
    ) {
    }

    @ApiModel(value = "GetCalendarByIdResponse_DrinkRecordDto")
    public record DrinkRecordDto(

            long id,
            DrinkType drinkType,
            DrinkUnit drinkUnit,
            float quantity
    ) {
    }

    @ApiModel(value = "GetCalendarByIdResponse_PhotoDto")
    public record PhotoDto(

            long id,
            long userId,
            String url
    ) {
    }
}
