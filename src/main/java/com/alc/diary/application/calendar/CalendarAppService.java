package com.alc.diary.application.calendar;

import com.alc.diary.domain.calendar.CalendarRepository;
import com.alc.diary.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class CalendarAppService {

    private final UserRepository userRepository;
    private final CalendarRepository calendarRepository;
}
