package com.alc.diary.domain.drink.repository;

import com.alc.diary.domain.drink.Drink;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Optional;

public interface DrinkRepository extends Repository<Drink, Long> {

    Optional<Drink> findById(long id);

    Drink save(Drink drink);

    List<Drink> findAll();

    Page<Drink> findAll(Pageable pageable);

    @Query("SELECT d " +
           "FROM Drink d " +
           "WHERE (d.creatorId = :userId OR d.isPublic = true) " +
           "AND d.categoryId = :categoryId")
    List<Drink> findCreatedOrPublicDrinksByCategoryId(long userId, long categoryId); // FIXME: 성능 개선 필요
}
