package com.alc.diary.application.calender.dto.response;

import com.alc.diary.domain.calender.Calender;
import com.alc.diary.domain.calender.model.DrinkModel;

import java.time.LocalDateTime;
import java.util.List;

public record SearchCalenderDayResponse(
        float totalDrinkCount,
        List<DayCalender> calenderResponse
) implements SearchCalenderResponse {
    public record DayCalender(
            long calenderId,
            String title,
            LocalDateTime drinkStartDateTime,
            LocalDateTime drinkEndDateTime,
            List<DrinkModel> drinkModels) {
        private static DayCalender dayCalenderOf(Calender calender) {
            return new DayCalender(
                    calender.getId(),
                    calender.getTitle(),
                    calender.getDrinkStartDateTime(),
                    calender.getDrinkEndDateTime(),
                    calender.getDrinkModels().isEmpty() ? null : calender.getDrinkModels());
        }
    }

    public static SearchCalenderDayResponse of(List<Calender> calender) {
        return new SearchCalenderDayResponse(
                (float) calender.stream().flatMap(calender1 -> calender1.getDrinkModels().stream())
                        .mapToDouble(DrinkModel::getQuantity).sum(),
                calender.stream().map(DayCalender::dayCalenderOf).toList()
        );
    }
}
