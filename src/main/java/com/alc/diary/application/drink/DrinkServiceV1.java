package com.alc.diary.application.drink;

import com.alc.diary.application.drink.dto.request.CreateDrinkRequest;
import com.alc.diary.domain.drink.Drink;
import com.alc.diary.domain.drink.DrinkError;
import com.alc.diary.domain.drink.enums.DrinkType;
import com.alc.diary.domain.drink.repository.DrinkRepository;
import com.alc.diary.domain.exception.DomainException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class DrinkServiceV1 {

    private final DrinkRepository drinkRepository;

    @Transactional
    public DrinkDto createDrink(long userId, CreateDrinkRequest request) {
        String drinkName = request.drinkName().trim();

        List<Drink> allDrinks = drinkRepository.findCreatedOrPublicDrinksByCategoryId(userId, request.drinkCategoryId());

        for (Drink drink : allDrinks) {
            if (drink.getName().equals(drinkName)) {
                return DrinkDto.fromDomainModel(drink);
            }
        }

        Drink newDrink = Drink.createCustomDrink(request.drinkCategoryId(), drinkName, userId);
        Drink savedDrink = drinkRepository.save(newDrink);
        return DrinkDto.fromDomainModel(savedDrink);
    }

    public List<DrinkDto> getAllDrinks() {
        return drinkRepository.findAll().stream()
                .map(DrinkDto::fromDomainModel)
                .toList();
    }

    public DrinkDto getDrinkById(long drinkId) {
        return drinkRepository.findById(drinkId)
                .map(DrinkDto::fromDomainModel)
                .orElseThrow(() -> new DomainException(DrinkError.NOT_FOUND));
    }

    @Transactional
    public long deleteDrinkById(long userId, long drinkId) {
        Drink drink = drinkRepository.findById(drinkId)
                .orElseThrow(() -> new DomainException(DrinkError.NOT_FOUND));

        if (drink.getCreatorId() == null || !drink.getCreatorId().equals(userId)) {
            throw new DomainException((DrinkError.NO_PERMISSION));
        }
        if (drink.getIsPublic()) {
            throw new DomainException(DrinkError.NO_PERMISSION);
        }
        if (drink.getType() != DrinkType.CUSTOM) {
            throw new DomainException(DrinkError.NO_PERMISSION);
        }
        drinkRepository.delete(drink);

        return drinkId;
    }

    public List<DrinkDto> getDrinksByIds(List<Long> drinkIds) {
        return drinkRepository.findByIdIn(drinkIds).stream()
                .map(DrinkDto::fromDomainModel)
                .toList();
    }
}
