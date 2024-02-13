package com.alc.diary.application.admin.drink;

import com.alc.diary.application.admin.drink.request.AdminCreateDrinkRequest;
import com.alc.diary.domain.drink.Drink;
import com.alc.diary.domain.drink.repository.DrinkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class AdminDrinkServiceV1 {

    private final DrinkRepository drinkRepository;

    @Transactional
    public DrinkDto createDrink(AdminCreateDrinkRequest request) {
        Drink drinkToSave = Drink.createBasicDrink(request.drinkCategoryId(), request.name());
        Drink drink = drinkRepository.save(drinkToSave);
        return DrinkDto.from(drink);
    }

    public Page<DrinkDto> getAllDrinks(Pageable pageable) {
        return drinkRepository.findAll(pageable)
                .map(DrinkDto::from);
    }
}
