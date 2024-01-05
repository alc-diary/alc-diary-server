package com.alc.diary.application.admin;

import com.alc.diary.application.admin.response.CalendarDto;
import com.alc.diary.domain.calendar.repository.CalendarRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class AdminService {

    private final CalendarRepository calendarRepository;

    public Page<CalendarDto> getAllCalendars(Pageable pageable) {
        return calendarRepository.findAll(pageable)
                .map(CalendarDto::fromDomainModel);
    }
}
