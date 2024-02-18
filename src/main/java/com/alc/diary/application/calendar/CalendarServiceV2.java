package com.alc.diary.application.calendar;

import com.alc.diary.application.calendar.dto.request.CreateCalendarRequestV2;
import com.alc.diary.application.calendar.dto.response.CreateCalendarResponseV2;
import com.alc.diary.application.calendar.dto.response.GetMonthlyCalendarsResponseV2;
import com.alc.diary.domain.calendar.Calendar;
import com.alc.diary.domain.calendar.DrinkRecord;
import com.alc.diary.domain.calendar.Photo;
import com.alc.diary.domain.calendar.UserCalendar;
import com.alc.diary.domain.calendar.enums.DrinkType;
import com.alc.diary.domain.calendar.enums.DrinkUnitType;
import com.alc.diary.domain.calendar.error.CalendarError;
import com.alc.diary.domain.calendar.error.DrinkRecordError;
import com.alc.diary.domain.calendar.repository.CalendarRepository;
import com.alc.diary.domain.drink.Drink;
import com.alc.diary.domain.drink.repository.DrinkRepository;
import com.alc.diary.domain.exception.DomainException;
import com.alc.diary.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class CalendarServiceV2 {

    private final UserRepository userRepository;
    private final CalendarRepository calendarRepository;
    private final DrinkRepository drinkRepository;

    @Transactional
    public CreateCalendarResponseV2 createCalendarAndGenerateResponse(long userId, CreateCalendarRequestV2 request) {
        validRequest(userId, request);
        Calendar calendarToSave = createCalendar(userId, request);
        List<Photo> photosToSave = createPhotos(userId, request);
        calendarToSave.addPhotos(photosToSave);

        List<UserCalendar> userCalendarsToSave = createUserCalendars(request);
        calendarToSave.addUserCalendars(userCalendarsToSave);

        Calendar calendar = calendarRepository.save(calendarToSave);

        return CreateCalendarResponseV2.from(calendar);
    }

    private void validRequest(long userId, CreateCalendarRequestV2 request) {
        if (userId != request.userCalendar().userId()) {
            throw new DomainException(CalendarError.INVALID_REQUEST);
        }
        boolean allValid = request.userCalendar().drinks().stream()
                .map(CreateCalendarRequestV2.DrinkCreationDto::quantity)
                .allMatch(CalendarServiceV2::isHalfUnit);
        if (!allValid) {
            throw new DomainException(CalendarError.INVALID_DRINK_QUANTITY_INCREMENT);
        }
    }

    private static boolean isHalfUnit(float value) {
        float multiplied = value * 2;
        float epsilon = 0.0001f;

        return Math.abs(multiplied - Math.round(multiplied)) < epsilon;
    }

    private static Calendar createCalendar(long userId, CreateCalendarRequestV2 request) {
        float totalQuantity = request.userCalendar().drinks().stream()
                .map(CreateCalendarRequestV2.DrinkCreationDto::quantity)
                .reduce(0f, Float::sum);
        return Calendar.create(userId, request.title(), totalQuantity, request.drinkDate());
    }

    private static List<Photo> createPhotos(long userId, CreateCalendarRequestV2 request) {
        if (CollectionUtils.size(request.photos()) > 20) {
            throw new DomainException((CalendarError.IMAGE_LIMIT_EXCEEDED));
        }
        return request.photos().stream()
                .map(photoDto -> Photo.create(userId, photoDto.url()))
                .toList();
    }

    private List<UserCalendar> createUserCalendars(CreateCalendarRequestV2 request) {
        List<UserCalendar> userCalendars = new ArrayList<>();

        UserCalendar userCalendar = UserCalendar.create(
                request.userCalendar().userId(),
                request.userCalendar().content(),
                request.userCalendar().condition());
        List<DrinkRecord> drinkRecords = createDrinkRecords(request.userCalendar());
        userCalendar.addDrinkRecords(drinkRecords);
        userCalendars.add(userCalendar);

        Set<Long> activeUserIds = userRepository.findActiveUserIdsByIdIn(request.taggedUserIds());
        List<UserCalendar> taggedUserCalendars = request.taggedUserIds().stream()
                .filter(activeUserIds::contains)
                .filter(it -> it != userCalendar.getUserId())
                .map(UserCalendar::createTaggedUserCalendar)
                .toList();
        userCalendars.addAll(taggedUserCalendars);
        return userCalendars;
    }

    private List<DrinkRecord> createDrinkRecords(CreateCalendarRequestV2.UserCalendarCreationDto userCalendarCreationDto) {
        // FIXME: 기존 API와 호환성 위해 이렇게 변경함. 수정해야 함.
        return userCalendarCreationDto.drinks().stream()
                .map(drinkDto -> {
                    DrinkType drinkType;
                    Drink drink = drinkRepository.findById(drinkDto.drinkId()).orElseThrow(() -> new DomainException(DrinkRecordError.NOT_FOUND));
                    if (drink.getCategoryId() == 1) {
                        drinkType = DrinkType.BEER;
                    } else if (drink.getCreatorId() == 2) {
                        drinkType = DrinkType.SOJU;
                    } else if (drink.getCreatorId() == 3) {
                        drinkType = DrinkType.WINE;
                    } else {
                        drinkType = DrinkType.MAKGEOLLI;
                    }

                    DrinkUnitType drinkUnit;
                    if (drinkDto.drinkUnitId() == 1) {
                        drinkUnit = DrinkUnitType.BOTTLE;
                    } else if (drinkDto.drinkUnitId() == 2) {
                        drinkUnit = DrinkUnitType.GLASS;
                    } else if (drinkDto.drinkUnitId() == 3) {
                        drinkUnit = DrinkUnitType.CAN;
                    } else {
                        drinkUnit = DrinkUnitType.ML_500;
                    }
                    return DrinkRecord.create(drinkType, drinkUnit, drinkDto.drinkId(), drinkDto.drinkUnitId(), drinkDto.quantity());
                })
                .toList();
    }

    public CalendarDto getCalendarById(long calendarId) {
        return calendarRepository.findById(calendarId)
                .map(CalendarDto::fromDomainModelWithUserCalendars)
                .orElseThrow(() -> new DomainException(CalendarError.CALENDAR_NOT_FOUND));
    }

    public List<CalendarDto> getDailyCalendars(long userId, LocalDate date) {
        List<Calendar> calendars = calendarRepository.findAllUserCalendarsInCalendarsWithInRangeAndUserId(userId, date, date);
        return calendars.stream()
                .map(CalendarDto::fromDomainModelWithUserCalendars)
                .toList();
    }

    public List<GetMonthlyCalendarsResponseV2> getMonthlyCalendars(long userId, YearMonth yearMonth) {
        LocalDate firstDay = yearMonth.atDay(1);
        LocalDate lastDay = yearMonth.atEndOfMonth();

        List<Calendar> calendars = calendarRepository.findAllUserCalendarsInCalendarsWithInRangeAndUserId(userId, firstDay, lastDay);

        Map<LocalDate, List<Calendar>> collect = calendars.stream()
                .collect(Collectors.groupingBy(Calendar::getDrinkDate));

        return collect.entrySet().stream()
                .map(entry -> GetMonthlyCalendarsResponseV2.of(entry.getKey(), entry.getValue()))
                .sorted(Comparator.comparing(GetMonthlyCalendarsResponseV2::date))
                .toList();
    }
}
