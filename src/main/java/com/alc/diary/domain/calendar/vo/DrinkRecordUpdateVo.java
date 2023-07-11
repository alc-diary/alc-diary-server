package com.alc.diary.domain.calendar.vo;

import com.alc.diary.domain.calendar.enums.DrinkType;
import com.alc.diary.domain.calendar.enums.DrinkUnit;

public record DrinkRecordUpdateVo(

        long id,
        DrinkType drinkType,
        DrinkUnit drinkUnit,
        float quantity
) {
}
