package com.alc.diary.presentation.api;

import com.alc.diary.application.drink.DrinkDto;
import com.alc.diary.application.drink.DrinkServiceV1;
import com.alc.diary.application.drink.dto.request.CreateDrinkRequest;
import com.alc.diary.presentation.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/v1/drinks")
@RestController
public class DrinkApiControllerV1 {

    private final DrinkServiceV1 service;

    @PostMapping
    public ApiResponse<DrinkDto> createDrink(
            @ApiIgnore @RequestAttribute long userId, @RequestBody CreateDrinkRequest request) {
        DrinkDto drink = service.createDrink(userId, request);
        return ApiResponse.getCreated(drink);
    }

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

    @DeleteMapping("/{drinkId}")
    public ApiResponse<Long> deleteDrinkById(@ApiIgnore @RequestAttribute long userId, @PathVariable long drinkId) {
        long deletedDrinkId = service.deleteDrinkById(userId, drinkId);
        return ApiResponse.getNoContent(deletedDrinkId);
    }

    @GetMapping("/batch")
    public ApiResponse<List<DrinkDto>> getDrinksByIds(@RequestParam List<Long> ids) {
        List<DrinkDto> drinks = service.getDrinksByIds(ids);
        return ApiResponse.getSuccess(drinks);
    }
}
