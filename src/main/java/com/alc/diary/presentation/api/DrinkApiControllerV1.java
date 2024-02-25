package com.alc.diary.presentation.api;

import com.alc.diary.application.drink.DrinkDto;
import com.alc.diary.application.drink.DrinkServiceV1;
import com.alc.diary.presentation.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/v1/drinks")
@RestController
public class DrinkApiControllerV1 {

    private final DrinkServiceV1 service;

    @GetMapping
    public ApiResponse<List<DrinkDto>> getAllDrinks() {
        List<DrinkDto> drinks = service.getAllDrinks();
        return ApiResponse.getSuccess(drinks);
    }

    @GetMapping("/{drinkId}")
    public ApiResponse<DrinkDto> getDrinkById(@PathVariable long drinkId) {
        DrinkDto drink = service.getDrinkById(drinkId);
        return ApiResponse.getSuccess(drink);
    }

    @GetMapping("/batch")
    public ApiResponse<List<DrinkDto>> getDrinksByIds(@RequestParam List<Long> ids) {
        List<DrinkDto> drinks = service.getDrinksByIds(ids);
        return ApiResponse.getSuccess(drinks);
    }
}
