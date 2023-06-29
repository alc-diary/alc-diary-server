package com.alc.diary.application.drinkunit;

import com.alc.diary.application.drinkunit.dto.request.CreateDrinkUnitRequest;
import com.alc.diary.application.drinkunit.dto.response.GetAllDrinkUnitsResponse;
import com.alc.diary.domain.drinkunit.DrinkUnit;
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
public class DrinkUnitService {

    private final DrinkUnitRepository drinkUnitRepository;

    /**
     * 음료 단위 생성
     *
     * @param request
     * @return
     */
    @Transactional
    public long createDrinkUnit(CreateDrinkUnitRequest request) {
        if (drinkUnitRepository.findByName(request.drinkUnitName()).isPresent()) {
            throw new DomainException(DrinkUnitError.DUPLICATE_NAME);
        }
        DrinkUnit drinkUnitToSave = new DrinkUnit(request.drinkUnitName());
        DrinkUnit drinkUnit = drinkUnitRepository.save(drinkUnitToSave);
        return drinkUnit.getId();
    }

    /**
     * 모든 음료 단위 조회
     *
     * @return
     */
    public List<GetAllDrinkUnitsResponse> getAllDrinkUnits() {
        return drinkUnitRepository.findAll().stream()
                .map(GetAllDrinkUnitsResponse::from)
                .toList();
    }

    /**
     * 음료 단위 삭제
     *
     * @param drinkUnitId
     */
    @Transactional
    public void deleteDrinkUnit(long drinkUnitId) {
        drinkUnitRepository.deleteById(drinkUnitId);
    }
}
