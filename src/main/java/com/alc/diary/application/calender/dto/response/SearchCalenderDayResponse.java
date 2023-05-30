package com.alc.diary.application.calender.dto.response;

import com.alc.diary.domain.calender.Calender;
import com.alc.diary.domain.calender.error.CalenderError;
import com.alc.diary.domain.calender.model.DrinkModel;
import com.alc.diary.domain.exception.CalenderException;

import java.time.LocalDateTime;
import java.util.Comparator;
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
            DrinkModel drinkModel) {
        private static DayCalender dayCalenderOf(Calender calender) {
            return new DayCalender(
                    calender.getId(),
                    calender.getTitle(),
                    calender.getDrinkStartDateTime(),
                    calender.getDrinkEndDateTime(),
                    getMaxDrinkModel(calender.getDrinkModels()));
        }

        private static DrinkModel getMaxDrinkModel(List<DrinkModel> drinkModels) {
            if (drinkModels.isEmpty()) throw new CalenderException(CalenderError.NO_ENTITY_FOUND);
            return DrinkModel.builder()
                    .type(drinkModels.stream().max(Comparator.comparing(DrinkModel::getQuantity)).get().getType())
                    .quantity((float) drinkModels.stream().mapToDouble(DrinkModel::getQuantity).sum())
                    .build();
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
