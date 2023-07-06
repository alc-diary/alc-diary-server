package com.alc.diary.domain.calendar.repository;

import com.alc.diary.domain.calendar.Calendar;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.util.Optional;

public interface CalendarRepository extends Repository<Calendar, Long> {

    Calendar save(Calendar calendar);

    @Query("SELECT DISTINCT c " +
            "FROM Calendar c " +
            "JOIN FETCH c.userCalendars uc " +
            "WHERE c.id = :calendarId " +
            "AND uc.deletedAt IS NULL " +
            "AND c.deletedAt IS NULL ")
    Optional<Calendar> findById(long calendarId);
}
