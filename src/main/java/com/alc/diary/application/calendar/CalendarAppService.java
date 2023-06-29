package com.alc.diary.application.calendar;

import com.alc.diary.application.calendar.dto.CalendarDto;
import com.alc.diary.application.calendar.dto.request.CreateCalendarAppRequest;
import com.alc.diary.application.calendar.dto.request.SearchCalendarAppRequest;
import com.alc.diary.application.calendar.dto.response.GetCalendarRequestsAppResponse;
import com.alc.diary.application.calendar.dto.response.GetMonthlyCalendarsAppResponse;
import com.alc.diary.application.calendar.dto.response.SearchCalendarAppResponse;
import com.alc.diary.domain.calendar.Calendar;
import com.alc.diary.domain.calendar.CalendarError;
import com.alc.diary.domain.calendar.CalendarRepository;
import com.alc.diary.domain.calendar.Calendars;
import com.alc.diary.domain.exception.DomainException;
import com.alc.diary.domain.user.User;
import com.alc.diary.domain.user.error.UserError;
import com.alc.diary.domain.user.repository.UserRepository;
import com.alc.diary.domain.usercalendar.UserCalendar;
import com.alc.diary.domain.usercalendar.UserCalendarImage;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class CalendarAppService {

    private final UserRepository userRepository;
    private final CalendarRepository calendarRepository;

    /**
     * 캘린더 생성
     *
     * @param userId
     * @param request
     */
    @Transactional
    public void createCalendar(long userId, CreateCalendarAppRequest request) {
    }

    @NotNull
    private static Calendar buildCalendarEntity(CreateCalendarAppRequest request, User foundUser) {
    }

    @NotNull
    private static UserCalendar buildUserCalendarEntity(CreateCalendarAppRequest request, User foundUser) {
    }

    private static void addUserCalendarImages(UserCalendar userCalendar, List<String> imageUrls) {
    }

    private static void addUserCalendarDrinks(UserCalendar userCalendar, List<CreateCalendarAppRequest.DrinkDto> drinks) {
    }

    private static void addCalendars(Calendar calendar, UserCalendar userCalendar, List<UserCalendar> taggedUserCalendars) {
    }

    /**
     * 캘린더 조회
     *
     * @param userId
     * @param calendarId
     * @return
     */
    @Cacheable(value = "findCalendar", key = "#userId + '_' + #calendarId", cacheManager = "cacheManager")
    public CalendarDto findCalendar(long userId, long calendarId) {
    }

    public List<CalendarDto> search(long userId, SearchCalendarAppRequest request) {
    }

    public GetMonthlyCalendarsAppResponse getMonthlyCalendars(long userId, Integer year, Integer month) {
    }

    public GetCalendarRequestsAppResponse getCalendarRequests(long userId) {
    }
}
