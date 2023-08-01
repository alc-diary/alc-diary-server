package com.alc.diary.domain.calendar.repository;

import com.alc.diary.domain.calendar.UserCalendar;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.util.Optional;

public interface UserCalendarRepository extends Repository<UserCalendar, Long> {

    @Query("SELECT uc " +
            "FROM UserCalendar uc " +
            "WHERE uc.id = :userCalendarId " +
            "AND uc.deletedAt IS NULL ")
    Optional<UserCalendar> findById(Long userCalendarId);
}
