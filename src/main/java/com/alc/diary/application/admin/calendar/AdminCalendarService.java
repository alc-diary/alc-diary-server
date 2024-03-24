package com.alc.diary.application.admin.calendar;

import com.alc.diary.application.admin.calendar.response.AdminCalendarDto;
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

    public Page<AdminCalendarDto> getAllCalendars(Pageable pageable) {
        return calendarRepository.findAll(pageable)
                .map(AdminCalendarDto::fromDomainModel);
    }

    public AdminCalendarDto getCalendarById(Long calendarId) {
        return calendarRepository.findById(calendarId)
                .map(AdminCalendarDto::fromDomainModel)
                .orElseThrow(() -> new DomainException(CalendarError.CALENDAR_NOT_FOUND));
    }
}
