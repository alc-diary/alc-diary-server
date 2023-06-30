package com.alc.diary.domain.calendar;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface CalendarRepository extends Repository<Calendar, Long> {

    Calendar save(Calendar calendar);

    Optional<Calendar> findById(long calendarId);
}
