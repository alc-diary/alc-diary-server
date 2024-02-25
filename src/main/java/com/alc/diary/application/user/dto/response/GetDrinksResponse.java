package com.alc.diary.application.user.dto.response;

import com.alc.diary.domain.drink.Drink;
import com.alc.diary.domain.drinkcategory.DrinkCategory;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public record GetDrinksResponse(

        long categoryId,
        String categoryName,
        List<GetDrinkResponse_DrinkDto> drinks
) {

    public record GetDrinkResponse_DrinkDto(
            long id,
            String name
    ) {
    }

    public static List<GetDrinksResponse> of(List<DrinkCategory> drinkCategories, List<Drink> drinks) {
        Map<Long, List<Drink>> idToDrinks = drinks.stream()
                .collect(Collectors.groupingBy(Drink::getCategoryId));
        return drinkCategories.stream()
                .map(drinkCategory -> new GetDrinksResponse(
                        drinkCategory.getId(),
                        drinkCategory.getName(),
                        idToDrinks.getOrDefault(drinkCategory.getId(), List.of()).stream()
                                .map(drink -> new GetDrinkResponse_DrinkDto(drink.getId(), drink.getName()))
                                .toList()
                ))
                .toList();
    }
}
