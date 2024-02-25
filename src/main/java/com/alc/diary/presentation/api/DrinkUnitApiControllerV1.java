package com.alc.diary.presentation.api;

import com.alc.diary.application.drinkunit.DrinkUnitDto;
import com.alc.diary.application.drinkunit.DrinkUnitServiceV1;
import com.alc.diary.presentation.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/v1/drink-units")
@RestController
public class DrinkUnitApiControllerV1 {

    private final DrinkUnitServiceV1 service;

    /**
     * 음료 단위를 가져온다.
     *
     * @return 음료 단위 목록
     */
    @GetMapping
    public ApiResponse<List<DrinkUnitDto>> getAllDrinkUnits() {
        return ApiResponse.getSuccess(service.getAllDrinkUnits());
    }

    @GetMapping("/batch")
    public ApiResponse<List<DrinkUnitDto>> getDrinkUnitsByIds(List<Long> drinkUnitIds) {
        return ApiResponse.getSuccess(service.getDrinkUnitsByIds(drinkUnitIds));
    }

    @GetMapping("/{drinkUnitId}")
    public ApiResponse<DrinkUnitDto> getDrinkUnitById(@PathVariable long drinkUnitId) {
        return ApiResponse.getSuccess(service.getDrinkUnitById(drinkUnitId));
    }
}
