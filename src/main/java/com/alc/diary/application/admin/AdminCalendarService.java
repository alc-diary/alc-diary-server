package com.alc.diary.application.admin;

import com.alc.diary.application.admin.response.CalendarDto;
import com.alc.diary.domain.calendar.error.CalendarError;
import com.alc.diary.domain.calendar.repository.CalendarRepository;
import com.alc.diary.domain.exception.DomainException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class AdminCalendarService {

    private final CalendarRepository calendarRepository;

    public Page<CalendarDto> getAllCalendars(Pageable pageable) {
        return calendarRepository.findAll(pageable)
                .map(CalendarDto::fromDomainModel);
    }

    public CalendarDto getCalendar(Long calendarId) {
        return calendarRepository.findById(calendarId)
                .map(CalendarDto::fromDomainModel)
                .orElseThrow(() -> new DomainException(CalendarError.CALENDAR_NOT_FOUND));
    }
}
