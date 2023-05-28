package com.alc.diary.domain.calender.repository;

import com.alc.diary.domain.calender.Calender;
import org.springframework.data.repository.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


public interface CalenderRepository extends Repository<Calender, Long> {
    Calender save(Calender calender);

    void deleteCalenderById(Long calenderId);

    Optional<Calender> getCalenderById(Long calenderId);

    List<Calender> findByUser_IdAndDrinkStartDateTimeGreaterThanEqualAndDrinkStartDateTimeLessThan(Long userId, LocalDateTime start, LocalDateTime end);
}
