package com.alc.diary.application.drinkunit;

import com.alc.diary.domain.drinkunit.DrinkUnitError;
import com.alc.diary.domain.drinkunit.DrinkUnitRepository;
import com.alc.diary.domain.exception.DomainException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class DrinkUnitServiceV1 {

    private final DrinkUnitRepository drinkUnitRepository;

    /**
     * 모든 음료 단위 조회
     *
     * @return
     */
    public List<DrinkUnitDto> getAllDrinkUnits() {
        return drinkUnitRepository.findAll().stream()
                .map(DrinkUnitDto::fromDomainModel)
                .toList();
    }

    public List<DrinkUnitDto> getDrinkUnitsByIds(List<Long> drinkUnitIds) {
        return drinkUnitRepository.findByIdIn(drinkUnitIds).stream()
                .map(DrinkUnitDto::fromDomainModel)
                .toList();
    }

    public DrinkUnitDto getDrinkUnitById(long drinkUnit) {
        return drinkUnitRepository.findById(drinkUnit)
                .map(DrinkUnitDto::fromDomainModel)
                .orElseThrow(() -> new DomainException(DrinkUnitError.NOT_FOUND));
    }
}
