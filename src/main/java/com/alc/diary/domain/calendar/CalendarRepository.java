package com.alc.diary.domain.calendar;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface CalendarRepository extends Repository<Calendar, Long> {

    Calendar save(Calendar calendar);

    @Query("SELECT DISTINCT c " +
            "FROM Calendar c " +
            "JOIN FETCH c.userCalendars uc " +
            "LEFT JOIN FETCH uc.images " +
            "WHERE c.id = :calendarId " +
            "AND uc.status = 'ACCEPTED'")
    Optional<Calendar> findByIdAndUserCalendars_StatusEqualAccepted(Long calendarId);

    @Query("SELECT DISTINCT c " +
            "FROM Calendar c " +
            "JOIN FETCH c.userCalendars uc " +
            "LEFT JOIN FETCH uc.images i " +
            "LEFT JOIN FETCH uc.drinks d " +
            "WHERE uc.user.id = :userId " +
            "AND c.drinkStartTime >= :startTime " +
            "AND c.drinkEndTime < :endTime " +
            "AND uc.status = 'ACCEPTED'")
    List<Calendar> findByUserIdAndDrinkStartTimeGreaterThanEqualAndDrinkStartTimeLessThanAndUserCalendars_StatusEqualAccepted(
            long userId,
            LocalDateTime startTime,
            LocalDateTime endTime
    );

    @Query("SELECT DISTINCT c " +
            "FROM Calendar c " +
            "JOIN FETCH c.userCalendars uc " +
            "JOIN FETCH c.owner o " +
            "JOIN FETCH o.detail d " +
            "WHERE uc.user.id = :userId " +
            "AND uc.status = 'PENDING'")
    List<Calendar> findByUserCalendar_UserIdAndUserCalendar_StatusEqualPending(long userId);
}
