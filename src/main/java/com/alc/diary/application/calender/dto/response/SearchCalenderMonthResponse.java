package com.alc.diary.application.calender.dto.response;

import com.alc.diary.domain.calender.Calender;
import com.alc.diary.domain.calender.enums.DrinkType;
import com.alc.diary.domain.calender.model.DrinkModel;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

public record SearchCalenderMonthResponse(
        List<MonthCalender> calenderResponse
) implements SearchCalenderResponse {
    public record MonthCalender(

            LocalDateTime drinkStartDateTime,
            DrinkType drinkType
    ) {
        private static MonthCalender monthOf(Calender calender) {
            return new MonthCalender(
                    calender.getDrinkStartDateTime(),
                    getMaxDrinkType(calender.getDrinkModels())
            );
        }

        private static DrinkType getMaxDrinkType(List<DrinkModel> drinkModels) {
            return drinkModels.stream().max(Comparator.comparing(DrinkModel::getQuantity)).orElseThrow().getType();
        }
    }

    public static SearchCalenderMonthResponse of(List<Calender> calender) {
        return new SearchCalenderMonthResponse(calender.stream().map(MonthCalender::monthOf).toList());
    }

}
