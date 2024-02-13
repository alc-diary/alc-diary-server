package com.alc.diary.application.calendar;

import com.alc.diary.application.calendar.dto.request.CreateCalendarRequestV2;
import com.alc.diary.application.calendar.dto.response.CreateCalendarResponseV2;
import com.alc.diary.domain.calendar.Calendar;
import com.alc.diary.domain.calendar.DrinkRecord;
import com.alc.diary.domain.calendar.Photo;
import com.alc.diary.domain.calendar.UserCalendar;
import com.alc.diary.domain.calendar.error.CalendarError;
import com.alc.diary.domain.calendar.repository.CalendarRepository;
import com.alc.diary.domain.exception.DomainException;
import com.alc.diary.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class CalendarServiceV2 {

    private final UserRepository userRepository;
    private final CalendarRepository calendarRepository;

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
        return Calendar.create(userId, request.title(), totalQuantity, ZonedDateTime.now(), ZonedDateTime.now());
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

    private static List<DrinkRecord> createDrinkRecords(CreateCalendarRequestV2.UserCalendarCreationDto userCalendarCreationDto) {
        return userCalendarCreationDto.drinks().stream()
                .map(drinkDto -> DrinkRecord.create(drinkDto.drinkId(), drinkDto.drinkUnitId(), drinkDto.quantity()))
                .toList();
    }
}
