package com.alc.diary.domain.calendar.repository;

import com.alc.diary.domain.calendar.UserCalendar;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Optional;

public interface UserCalendarRepository extends Repository<UserCalendar, Long> {

    UserCalendar save(UserCalendar userCalendar);

    @Query("SELECT uc " +
            "FROM UserCalendar uc " +
            "WHERE uc.id = :id " +
            "AND uc.isDeleted = FALSE ")
    Optional<UserCalendar> findByIdAndIsDeletedEqFalse(long id);
}
