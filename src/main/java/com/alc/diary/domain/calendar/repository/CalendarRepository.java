package com.alc.diary.domain.calendar.repository;

import com.alc.diary.domain.calendar.Calendar;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

public interface CalendarRepository extends Repository<Calendar, Long> {

    Calendar save(Calendar calendar);

    @Query("SELECT DISTINCT c " +
            "FROM Calendar c " +
            "JOIN FETCH c.userCalendars uc " +
            "WHERE c.id = :calendarId " +
            "AND uc.isDeleted IS FALSE ")
    Optional<Calendar> findById(long calendarId);

    @Query("SELECT DISTINCT c " +
           "FROM Calendar c " +
           "JOIN c.userCalendars uc " +
           "JOIN FETCH c.userCalendars " +
           "WHERE uc.userId = :userId " +
           "AND c.drinkStartTime >= :rangeStart " +
           "AND c.drinkStartTime < :rangeEnd " +
           "AND uc.isDeleted IS FALSE ")
    List<Calendar> findCalendarsWithInRangeAndUserId(long userId, ZonedDateTime rangeStart, ZonedDateTime rangeEnd);

    @Query("SELECT DISTINCT c " +
           "FROM Calendar c " +
           "JOIN FETCH c.userCalendars uc " +
           "WHERE uc.userId = :userId " +
           "AND c.drinkStartTime >= :rangeStart " +
           "AND c.drinkStartTime < :rangeEnd " +
           "AND uc.isDeleted IS FALSE ")
    List<Calendar> findCalendarsWithInRangeForSpecificUser(long userId, ZonedDateTime rangeStart, ZonedDateTime rangeEnd);
}
