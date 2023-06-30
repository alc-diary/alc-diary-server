package com.alc.diary.domain.calendar.repository;

import com.alc.diary.domain.calendar.UserCalendar;
import org.springframework.data.repository.Repository;

import java.util.List;

public interface UserCalendarRepository extends Repository<UserCalendar, Long> {

    UserCalendar save(UserCalendar userCalendar);

    List<UserCalendar> saveAll(Iterable<UserCalendar> userCalendars);
}
