package com.alc.diary.domain.categoryunit;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryUnitRepository extends JpaRepository<CategoryUnit, Long> {

    List<CategoryUnit> findByCategoryId(long categoryId);
}
