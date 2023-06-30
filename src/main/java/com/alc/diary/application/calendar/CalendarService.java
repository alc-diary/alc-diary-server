package com.alc.diary.application.calendar;

import com.alc.diary.application.calendar.dto.request.CreateCalendarRequest;
import com.alc.diary.application.calendar.dto.response.CreateCalendarResponse;
import com.alc.diary.application.calendar.dto.response.GetCalendarByIdResponse;
import com.alc.diary.domain.calendar.Calendar;
import com.alc.diary.domain.calendar.error.CalendarError;
import com.alc.diary.domain.calendar.repository.CalendarRepository;
import com.alc.diary.domain.drink.DrinkUnitInfo;
import com.alc.diary.domain.drink.repository.DrinkUnitInfoRepository;
import com.alc.diary.domain.exception.DomainException;
import com.alc.diary.domain.user.repository.UserRepository;
import com.alc.diary.domain.calendar.UserCalendar;
import com.alc.diary.domain.calendar.UserCalendarDrink;
import com.alc.diary.domain.calendar.UserCalendarImage;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
}
