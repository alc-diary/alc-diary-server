package com.alc.diary.domain.calendar.repository;

import com.alc.diary.domain.calendar.Calendar;
import com.alc.diary.domain.calender.Calender;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

public interface CalendarRepository extends JpaRepository<Calendar, Long> {

    Calendar save(Calendar calendar);

    @Query("SELECT DISTINCT c " +
           "FROM Calendar c " +
           "JOIN FETCH c.userCalendars uc " +
           "WHERE c.id = :calendarId " +
           "AND c.deletedAt IS NULL " +
           "AND uc.deletedAt IS NULL ")
    Optional<Calendar> findById(long calendarId);

    @Query("SELECT DISTINCT c " +
           "FROM Calendar c " +
           "JOIN FETCH c.photos p " +
           "WHERE c.id = :calendarId " +
           "AND c.deletedAt IS NULL " +
           "AND p.deletedAt IS NULL ")
    Optional<Calendar> findWithPhotoById(long calendarId);

    // @Query("SELECT DISTINCT c " +
    //        "FROM Calendar c " +
    //        "JOIN c.userCalendars uc " +
    //        "JOIN FETCH c.userCalendars " +
    //        "WHERE uc.userId = :userId " +
    //        "AND c.drinkStartTime >= :rangeStart " +
    //        "AND c.drinkStartTime < :rangeEnd " +
    //        "AND uc.deletedAt IS NULL " +
    //        "AND c.deletedAt IS NULL")
    // List<Calendar> findAllUserCalendarsInCalendarsWithInRangeAndUserId(long userId, ZonedDateTime rangeStart, ZonedDateTime rangeEnd);

    @Query("SELECT DISTINCT c " +
           "FROM Calendar c " +
           "JOIN FETCH c.userCalendars uc " +
           "WHERE EXISTS (SELECT 1 FROM UserCalendar uc2 WHERE uc2.calendar = c AND uc2.userId = :userId AND uc2.deletedAt IS NULL) " +
           "AND c.drinkStartTime >= :rangeStart " +
           "AND c.drinkEndTime < :rangeEnd " +
           "AND uc.deletedAt IS NULL " +
           "AND c.deletedAt IS NULL")
    List<Calendar> findAllUserCalendarsInCalendarsWithInRangeAndUserId(long userId, ZonedDateTime rangeStart, ZonedDateTime rangeEnd);

    @Query("SELECT DISTINCT c " +
           "FROM Calendar c " +
           "JOIN FETCH c.userCalendars uc " +
           "WHERE uc.userId = :userId " +
           "AND c.drinkStartTime >= :rangeStart " +
           "AND c.drinkStartTime < :rangeEnd " +
           "AND uc.deletedAt IS NULL " +
           "AND c.deletedAt IS NULL")
    List<Calendar> findUserCalendarsForSpecificUserWithRangeAndUserId(long userId, ZonedDateTime rangeStart, ZonedDateTime rangeEnd);
}
