package com.alc.diary.application.calendar;

import com.alc.diary.application.calendar.dto.CalendarDto;
import com.alc.diary.application.calendar.dto.request.FindCalendarAppResponse;
import com.alc.diary.application.calendar.dto.request.SaveCalendarAppRequest;
import com.alc.diary.application.calendar.dto.request.SearchCalendarAppRequest;
import com.alc.diary.application.calendar.dto.response.GetCalendarRequestsAppResponse;
import com.alc.diary.application.calendar.dto.response.GetMonthlyCalendarsAppResponse;
import com.alc.diary.application.calendar.dto.response.SearchCalendarAppResponse;
import com.alc.diary.domain.calendar.Calendar;
import com.alc.diary.domain.calendar.CalendarRepository;
import com.alc.diary.domain.calendar.Calendars;
import com.alc.diary.domain.calender.error.CalenderError;
import com.alc.diary.domain.drink.UserCalendarDrink;
import com.alc.diary.domain.exception.DomainException;
import com.alc.diary.domain.user.User;
import com.alc.diary.domain.user.error.UserError;
import com.alc.diary.domain.user.repository.UserRepository;
import com.alc.diary.domain.usercalendar.UserCalendar;
import com.alc.diary.domain.usercalendar.UserCalendarImage;
import com.alc.diary.domain.usercalendar.UserCalendarStatus;
import lombok.RequiredArgsConstructor;
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
    public void save(long userId, SaveCalendarAppRequest request) {
        User foundUser =
                userRepository.findById(userId).orElseThrow(() -> new DomainException(UserError.USER_NOT_FOUND));
        List<UserCalendar> taggedUserCalendarsToSave = userRepository.findByIdIn(request.taggedUserId()).stream()
                .map(UserCalendar::createUserCalendarRequest)
                .toList();

        Calendar calendarToSave =
                new Calendar(foundUser, request.title(), request.drinkStartTime(), request.drinkEndTime());
        UserCalendar userCalendarToSave =
                new UserCalendar(foundUser, request.content(), request.condition(), UserCalendarStatus.ACCEPTED);
        List<UserCalendarImage> userCalendarImagesToSave = request.imageUrls().stream()
                .map(UserCalendarImage::new)
                .toList();
        userCalendarToSave.addImages(userCalendarImagesToSave);

        List<UserCalendarDrink> userCalendarDrinksToSave = request.drinks().stream()
                .map(drinkDto -> new UserCalendarDrink(drinkDto.drink(), drinkDto.quantity()))
                .collect(Collectors.toList());
        userCalendarToSave.addDrinks(userCalendarDrinksToSave);

        calendarToSave.addUserCalendar(userCalendarToSave);
        calendarToSave.addUserCalendars(taggedUserCalendarsToSave);

        calendarRepository.save(calendarToSave);
    }

    /**
     * 캘린더 조회
     *
     * @param userId
     * @param calendarId
     * @return
     */
    public CalendarDto find(long userId, long calendarId) {
        Calendar foundCalendar =
                calendarRepository.findByIdAndUserCalendars_StatusEqualAccepted(calendarId)
                        .orElseThrow(() -> new DomainException(CalenderError.NO_ENTITY_FOUND));

        return CalendarDto.from(foundCalendar);
    }

    public SearchCalendarAppResponse search(long userId, SearchCalendarAppRequest request) {
        return SearchCalendarAppResponse.from(
                calendarRepository.findByUserIdAndDrinkStartTimeGreaterThanEqualAndDrinkStartTimeLessThanAndUserCalendars_StatusEqualAccepted(
                        userId,
                        request.getStartTime(),
                        request.getEndTime()
                ));
    }

    public GetMonthlyCalendarsAppResponse getMonthlyCalendars(long userId, Integer year, Integer month) {
        LocalDate date = LocalDate.of(year, month, 1);
        LocalDateTime startOfMonth = date.atStartOfDay();
        LocalDate nextMonth = date.plusMonths(1);
        LocalDateTime startOfNextMonth = nextMonth.atStartOfDay();
        Calendars calendars = Calendars.from(calendarRepository.findByUserIdAndDrinkStartTimeGreaterThanEqualAndDrinkStartTimeLessThanAndUserCalendars_StatusEqualAccepted(
                userId,
                startOfMonth,
                startOfNextMonth
        ));
        return GetMonthlyCalendarsAppResponse.from(calendars.getMonthlyCalendar());
    }

    public GetCalendarRequestsAppResponse getCalendarRequests(long userId) {
        return GetCalendarRequestsAppResponse.from(
                calendarRepository.findByUserCalendar_UserIdAndUserCalendar_StatusEqualPending(userId)
        );
    }
}
