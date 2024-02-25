package com.alc.diary.application.drink;

import com.alc.diary.domain.drink.DrinkError;
import com.alc.diary.domain.drink.repository.DrinkRepository;
import com.alc.diary.domain.exception.DomainException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class DrinkServiceV1 {

    private final DrinkRepository drinkRepository;

    public List<DrinkDto> getAllDrinks() {
        return drinkRepository.findAll().stream()
                .map(DrinkDto::from)
                .toList();
    }

    public DrinkDto getDrinkById(long drinkId) {
        return drinkRepository.findById(drinkId)
                .map(DrinkDto::from)
                .orElseThrow(() -> new DomainException(DrinkError.NOT_FOUND));
    }

    public List<DrinkDto> getDrinksByIds(List<Long> drinkIds) {
        return drinkRepository.findByIdIn(drinkIds).stream()
                .map(DrinkDto::from)
                .toList();
    }
}
