package com.alc.diary.application.admin.drinkunit;

import com.alc.diary.application.admin.drinkunit.request.AdminCreateDrinkUnitRequestV1;
import com.alc.diary.domain.drinkunit.DrinkUnit;
import com.alc.diary.domain.drinkunit.DrinkUnitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class AdminDrinkUnitServiceV1 {

    private final DrinkUnitRepository drinkUnitRepository;

    @Transactional
    public DrinkUnitDto createDrinkUnit(AdminCreateDrinkUnitRequestV1 request) {
        DrinkUnit drinkUnitToSave = new DrinkUnit(request.name());
        DrinkUnit drinkUnit = drinkUnitRepository.save(drinkUnitToSave);
        return DrinkUnitDto.from(drinkUnit);
    }

    public Page<DrinkUnitDto> getAllDrinkUnits(Pageable pageable) {
        return drinkUnitRepository.findAll(pageable)
                .map(DrinkUnitDto::from);
    }
}
