package com.alc.diary.domain.calendar.repository;

import com.alc.diary.domain.calendar.Calendar;
import org.springframework.data.repository.Repository;

public interface CalendarRepository extends Repository<Calendar, Long> {

    Calendar save(Calendar calendar);
}
