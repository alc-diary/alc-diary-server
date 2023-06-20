package com.alc.diary.application.calendar.dto.request;

import com.alc.diary.domain.drink.Drink;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

public record CreateCalendarAppRequest(

        @NotNull(message = "제목은 필수입니다.")
        String title,

        @Size(max = 1000, message = "내용은 1000자 이하여야 합니다.")
        String content,

        String condition,

        @NotNull(message = "음주 기록은 null을 허용하지 않습니다.")
        List<DrinkDto> drinks,

        @NotNull(message = "이미지 URL은 null을 허용하지 않습니다.")
        @Size(max = 5, message = "이미지는 최대 5개까지 저장 가능합니다.")
        List<String> imageUrls,

        @NotNull(message = "음주 시작 시간은 필수입니다.")
        LocalDateTime drinkStartTime,

        @NotNull(message = "음주 종료 시간은 필수입니다.")
        LocalDateTime drinkEndTime,

        @NotNull(message = "태그 목록은 null을 허용하지 않습니다.")
        List<Long> taggedUserId
) {

        public record DrinkDto(

                Drink drink,
                float quantity
        ) {
        }

}
