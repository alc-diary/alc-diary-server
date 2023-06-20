package com.alc.diary.application.calendar;

import com.alc.diary.application.calendar.dto.CalendarDto;
import com.alc.diary.application.calendar.dto.request.CreateCalendarAppRequest;
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
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
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
        User foundUser =
                userRepository.findById(userId).orElseThrow(() -> new DomainException(UserError.USER_NOT_FOUND));
        List<UserCalendar> taggedUserCalendarsToSave = userRepository.findByIdIn(request.taggedUserId()).stream()
                .map(UserCalendar::createUserCalendarRequest)
                .toList();

        Calendar calendarToSave = buildCalendarEntity(request, foundUser);
        UserCalendar userCalendarToSave = buildUserCalendarEntity(request, foundUser);

        addUserCalendarImages(userCalendarToSave, request.imageUrls());
        addUserCalendarDrinks(userCalendarToSave, request.drinks());

        addCalendars(calendarToSave, userCalendarToSave, taggedUserCalendarsToSave);

        calendarRepository.save(calendarToSave);
    }

    @NotNull
    private static Calendar buildCalendarEntity(CreateCalendarAppRequest request, User foundUser) {
        return new Calendar(foundUser, request.title(), request.drinkStartTime(), request.drinkEndTime());
    }

    @NotNull
    private static UserCalendar buildUserCalendarEntity(CreateCalendarAppRequest request, User foundUser) {
        return UserCalendar.create(foundUser, request.content(), request.condition());
    }

    private static void addUserCalendarImages(UserCalendar userCalendar, List<String> imageUrls) {
        List<UserCalendarImage> userCalendarImagesToSave = imageUrls.stream()
                .map(UserCalendarImage::new)
                .toList();
        userCalendar.addImages(userCalendarImagesToSave);
    }

    private static void addUserCalendarDrinks(UserCalendar userCalendar, List<CreateCalendarAppRequest.DrinkDto> drinks) {
        List<UserCalendarDrink> userCalendarDrinksToSave = drinks.stream()
                .map(drinkDto -> new UserCalendarDrink(drinkDto.drink(), drinkDto.quantity()))
                .collect(Collectors.toList());
        userCalendar.addDrinks(userCalendarDrinksToSave);
    }

    private static void addCalendars(Calendar calendar,  UserCalendar userCalendar, List<UserCalendar> taggedUserCalendars) {
        calendar.addUserCalendar(userCalendar);
        calendar.addUserCalendars(taggedUserCalendars);
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

    public List<CalendarDto> search(long userId, SearchCalendarAppRequest request) {
        SearchCalendarAppResponse.from(
                calendarRepository.findByUserIdAndDrinkStartTimeGreaterThanEqualAndDrinkStartTimeLessThanAndUserCalendars_StatusEqualAccepted(
                        userId,
                        request.getStartTime(),
                        request.getEndTime()
                ));
        return null;
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
