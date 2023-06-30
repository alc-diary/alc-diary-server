package com.alc.diary.application.calendar;

import com.alc.diary.application.calendar.dto.request.CreateCalendarRequest;
import com.alc.diary.application.calendar.dto.response.CreateCalendarResponse;
import com.alc.diary.domain.calendar.Calendar;
import com.alc.diary.domain.calendar.CalendarRepository;
import com.alc.diary.domain.drink.DrinkUnitInfo;
import com.alc.diary.domain.drink.repository.DrinkUnitInfoRepository;
import com.alc.diary.domain.user.User;
import com.alc.diary.domain.user.repository.UserRepository;
import com.alc.diary.domain.usercalendar.UserCalendar;
import com.alc.diary.domain.usercalendar.UserCalendarDrink;
import com.alc.diary.domain.usercalendar.UserCalendarImage;
import com.alc.diary.domain.usercalendar.repository.UserCalendarRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class CalendarAppService {

    private final UserRepository userRepository;
    private final CalendarRepository calendarRepository;
    private final UserCalendarRepository userCalendarRepository;
    private final DrinkUnitInfoRepository drinkUnitInfoRepository;

    @Transactional
    public CreateCalendarResponse createCalendar(long userId, CreateCalendarRequest request) {
        User user = userRepository.findActiveUserById(userId).orElseThrow();
        Calendar calendarToSave = Calendar.create(user.getId(), request.title(), request.drinkStartTime(), request.drinkEndTime());
        Calendar calendar = calendarRepository.save(calendarToSave);

        UserCalendar userCalendarToSave = UserCalendar.create(user.getId(), calendar.getId(), request.content(), request.drinkCondition());

        List<UserCalendar> taggedUsersCalendarToSave = request.taggedUserIds().stream()
                .map(taggedUserId -> UserCalendar.create(taggedUserId, calendar.getId(), null, null))
                .toList();

        List<UserCalendarImage> userCalendarImagesToSave = request.userCalendarImages().stream()
                .map(dto -> new UserCalendarImage(dto.imageUrl()))
                .toList();
        Map<Long, DrinkUnitInfo> drinkUnitInfoById = drinkUnitInfoRepository.findByIdIn(
                        request.userCalendarDrinks().stream().map(CreateCalendarRequest.UserCalendarDrinkDto::drinkUnitInfoId).toList()
                ).stream()
                .collect(Collectors.toMap(DrinkUnitInfo::getId, Function.identity()));
        List<UserCalendarDrink> userCalendarDrinksToSave = request.userCalendarDrinks().stream()
                .map(dto -> {
                    DrinkUnitInfo drinkUnitInfo = drinkUnitInfoById.get(dto.drinkUnitInfoId());
                    return new UserCalendarDrink(drinkUnitInfo.getId(), drinkUnitInfo.getPrice(), drinkUnitInfo.getCalories(), dto.quantity());
                })
                .toList();
        userCalendarToSave.addImages(userCalendarImagesToSave);
        userCalendarToSave.addDrinks(userCalendarDrinksToSave);

        UserCalendar userCalendar = userCalendarRepository.save(userCalendarToSave);
        List<UserCalendar> taggedUserCalendars = userCalendarRepository.saveAll(taggedUsersCalendarToSave);

        return new CreateCalendarResponse(
                calendar.getId(),
                Stream.concat(
                        Stream.of(userCalendar.getId()),
                        taggedUserCalendars.stream().map(UserCalendar::getId)
                ).toList()
        );
    }
}
