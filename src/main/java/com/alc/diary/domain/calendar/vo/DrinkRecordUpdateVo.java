package com.alc.diary.domain.calendar.vo;

import com.alc.diary.domain.calendar.enums.DrinkType;
import com.alc.diary.domain.calendar.enums.DrinkUnitType;

public record DrinkRecordUpdateVo(

        long id,
        DrinkType drinkType,
        DrinkUnitType drinkUnit,
        float quantity
) {
}
