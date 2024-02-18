package com.alc.diary.application.calendar.dto.request;

import java.time.LocalDate;
import java.util.List;

public record UpdateCalendarRequestV2(

        String title, // 필수, null 이면 업데이트 x
        LocalDate drinkDate, // 필수, null 이면 업데이트 x

        List<UserCalendarDto> userCalendars,
        List<Long> deleteUserCalendars,

        List<PhotoDto> photos,
        List<Long> deletePhotos
) {

    public record UserCalendarDto(

            Long id, // null 이면 추가, null 이 아니면 수정
            Long userId,
            boolean contentShouldBeUpdated,
            String content, // contentShouldBeUpdated가 true이고 null 이면 업데이트 x
            boolean drinkConditionShouldBeUpdated,
            String drinkCondition, // drinkConditionShouldBeUpdated가 true이고 null 이면 업데이트 x

            List<DrinkRecordDto> drinkRecords,
            List<Long> deleteDrinkRecords
    ) {

        public record DrinkRecordDto(

                Long id, // null 이면 추가, null 이 아니면 수정
                Long drinkId,
                Long drinkUnitId,
                Float quantity
        ) {
        }
    }

    public record PhotoDto(
            Long id,
            String url
    ) {
    }
}
