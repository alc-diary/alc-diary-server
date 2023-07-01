package com.alc.diary.application.calendar;

import com.alc.diary.application.calendar.dto.request.CreateCalendarRequest;
import com.alc.diary.application.calendar.dto.response.CreateCalendarResponse;
import com.alc.diary.application.calendar.dto.response.GetCalendarByIdResponse;
import com.alc.diary.application.calendar.dto.response.GetDailyCalendarsResponse;
import com.alc.diary.application.calendar.dto.response.GetMonthlyCalendarsResponse;
import com.alc.diary.domain.calendar.Calendar;
import com.alc.diary.domain.calendar.error.CalendarError;
import com.alc.diary.domain.calendar.error.UserCalendarError;
import com.alc.diary.domain.calendar.repository.CalendarRepository;
import com.alc.diary.domain.calendar.repository.UserCalendarRepository;
import com.alc.diary.domain.drink.DrinkUnitInfo;
import com.alc.diary.domain.drink.repository.DrinkUnitInfoRepository;
import com.alc.diary.domain.exception.DomainException;
import com.alc.diary.domain.user.User;
import com.alc.diary.domain.user.repository.UserRepository;
import com.alc.diary.domain.calendar.UserCalendar;
import com.alc.diary.domain.calendar.UserCalendarDrink;
import com.alc.diary.domain.calendar.UserCalendarImage;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class CalendarService {

    private final UserRepository userRepository;
    private final CalendarRepository calendarRepository;
    private final DrinkUnitInfoRepository drinkUnitInfoRepository;
    private final UserCalendarRepository userCalendarRepository;

    /**
     * 캘린더 생성
     *
     * @param userId
     * @param request
     * @return
     */
    @Transactional
    public CreateCalendarResponse createCalendar(long userId, CreateCalendarRequest request) {
        Calendar calendarToSave = createNewCalendar(userId, request);

        Calendar calendar = calendarRepository.save(calendarToSave);

        return new CreateCalendarResponse(
                calendar.getId(),
                calendar.getUserCalendars().stream()
                        .map(UserCalendar::getId)
                        .toList()
        );
    }

    @NotNull
    private Calendar createNewCalendar(long userId, CreateCalendarRequest request) {
        Calendar calendarToSave =
                Calendar.create(userId, request.title(), request.drinkStartTime(), request.drinkEndTime());

        UserCalendar userCalendarToSave = createUserCalendar(userId, request);
        List<UserCalendar> taggedUsersCalendarsToSave = createUserCalendarsForTaggedUsers(userId, request);

        calendarToSave.addUserCalendar(userCalendarToSave);
        calendarToSave.addUserCalendars(taggedUsersCalendarsToSave);

        return calendarToSave;
    }

    @NotNull
    private UserCalendar createUserCalendar(long userId, CreateCalendarRequest request) {
        UserCalendar userCalendarToSave = UserCalendar.create(userId, request.content(), request.drinkCondition());

        List<Long> drinkUnitInfoIds = request.userCalendarDrinks().stream()
                .map(CreateCalendarRequest.UserCalendarDrinkDto::drinkUnitInfoId)
                .toList();
        List<UserCalendarDrink> userCalendarDrinksToSave =
                createUserCalendarDrinks(drinkUnitInfoIds, request.userCalendarDrinks());
        List<UserCalendarImage> userCalendarImagesToSave = createUserCalendarImages(request.userCalendarImages());
        userCalendarToSave.addImages(userCalendarImagesToSave);
        userCalendarToSave.addDrinks(userCalendarDrinksToSave);

        return userCalendarToSave;
    }

    @NotNull
    private List<UserCalendarDrink> createUserCalendarDrinks(
            List<Long> drinkUnitInfoIds,
            List<CreateCalendarRequest.UserCalendarDrinkDto> userCalendarDrinkDtos
    ) {
        Map<Long, DrinkUnitInfo> drinkUnitInfoById = drinkUnitInfoRepository.findByIdIn(drinkUnitInfoIds).stream()
                .collect(Collectors.toMap(DrinkUnitInfo::getId, Function.identity()));
        return userCalendarDrinkDtos.stream()
                .map(dto -> {
                    DrinkUnitInfo drinkUnitInfo = drinkUnitInfoById.get(dto.drinkUnitInfoId());
                    return new UserCalendarDrink(
                            drinkUnitInfo.getId(),
                            drinkUnitInfo.getPrice(),
                            drinkUnitInfo.getCalories(),
                            dto.quantity()
                    );
                })
                .toList();
    }

    @NotNull
    private static List<UserCalendarImage> createUserCalendarImages(List<CreateCalendarRequest.UserCalendarImageDto> userCalendarImageDtos) {
        return userCalendarImageDtos.stream()
                .map(dto -> new UserCalendarImage(dto.imageUrl()))
                .toList();
    }

    @NotNull
    private List<UserCalendar> createUserCalendarsForTaggedUsers(long userId, CreateCalendarRequest request) {
        Set<Long> activeUserIds = userRepository.findActiveUserIdsByIdIn(request.taggedUserIds());
        return request.taggedUserIds().stream()
                .filter(taggedUserId -> taggedUserId != userId)
                .filter(activeUserIds::contains)
                .map(UserCalendar::createForTaggedUser)
                .toList();
    }

    /**
     * 캘린더 상세 조회
     *
     * @param calendarId
     * @return
     */
    public GetCalendarByIdResponse getCalendarByIdResponse(long userId, long calendarId) {
        return calendarRepository.findById(calendarId)
                .filter(calendar -> calendar.isInvolvedUser(userId))
                .map(calendar -> GetCalendarByIdResponse.of(calendar, userId))
                .orElseThrow(() -> new DomainException(CalendarError.CALENDAR_NOT_FOUND));
    }

    /**
     * 요청한 유저의 캘린더 데이터 삭제 (캘린더 삭제 x, 캘린더 데이터 중 자신의 데이터만 삭제)
     *
     * @param userId
     * @param userCalendarId
     */
    @Transactional
    public void deleteUserCalendar(long userId, long userCalendarId) {
        UserCalendar userCalendar = userCalendarRepository.findByIdAndIsDeletedEqFalse(userCalendarId)
                .orElseThrow(() -> new DomainException(UserCalendarError.USER_CALENDAR_NOT_FOUND));
        userCalendar.delete(userId);
    }

    /**
     * 해당 유저의 캘린더 조회(일별)
     *
     * @param userId
     * @param date
     * @return
     */
    public List<GetDailyCalendarsResponse> getDailyCalendars(long userId, LocalDate date) {
        LocalDateTime rangeStart = date.atStartOfDay();
        LocalDateTime rangeEnd = date.plusDays(1).atStartOfDay();
        List<Calendar> calendars = calendarRepository.findCalendarsWithInRangeAndUserId(userId, rangeStart, rangeEnd);
        Set<Long> userIds = calendars.stream()
                .flatMap(calendar -> calendar.getUserCalendarsExcludingUser(userId).stream())
                .map(UserCalendar::getUserId)
                .collect(Collectors.toSet());
        Map<Long, User> userById = userRepository.findActiveUsersByIdIn(userIds).stream()
                .collect(Collectors.toMap(User::getId, Function.identity()));
        return calendars.stream()
                .map(calendar ->
                        calendar.getUserCalendarOfUser(userId).map(userCalendar ->
                                new GetDailyCalendarsResponse(
                                        calendar.getId(),
                                        calendar.getTitle(),
                                        calendar.getDrinkStartTime().toString(),
                                        calendar.getDrinkEndTime().toString(),
                                        userCalendar.getAllDrinkUnitInfoIds(),
                                        userCalendar.getTotalQuantity(),
                                        calendar.getUserCalendarsExcludingUser(userId).stream()
                                                .map(taggedUserCalendar -> {
                                                    User taggedUser = userById.get(taggedUserCalendar.getUserId());
                                                    return new GetDailyCalendarsResponse.UserDto(
                                                            taggedUser.getId(),
                                                            taggedUser.getProfileImage()
                                                    );
                                                })
                                                .toList()
                                )
                        ).orElse(null)
                )
                .toList();
    }

    /**
     * 해당 유저의 캘린더 조회(월별)
     *
     * @param userId
     * @param month
     * @return
     */
    public List<GetMonthlyCalendarsResponse> getMonthlyCalendars(long userId, YearMonth month) {
        LocalDateTime rangeStart = month.atDay(1).atStartOfDay();
        LocalDateTime rangeEnd = month.plusMonths(1).atDay(1).atStartOfDay();
        List<Calendar> calendars = calendarRepository.findCalendarsWithInRangeAndUserId(userId, rangeStart, rangeEnd);
        return calendars.stream()
                .map(calendar -> new GetMonthlyCalendarsResponse(
                        calendar.getDate().toString(),
                        calendar.getUserCalendarOfUser(userId).map(userCalendar -> userCalendar.getDrinks().get(0).getDrinkUnitInfoId())
                                .orElse(0L)
                ))
                .distinct()
                .sorted()
                .toList();
    }
}
