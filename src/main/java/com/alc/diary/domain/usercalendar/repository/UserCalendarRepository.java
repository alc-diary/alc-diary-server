package com.alc.diary.domain.usercalendar.repository;

import com.alc.diary.domain.usercalendar.UserCalendar;
import org.springframework.data.repository.Repository;

import java.util.List;

public interface UserCalendarRepository extends Repository<UserCalendar, Long> {

    UserCalendar save(UserCalendar userCalendar);

    List<UserCalendar> saveAll(Iterable<UserCalendar> userCalendars);
}
