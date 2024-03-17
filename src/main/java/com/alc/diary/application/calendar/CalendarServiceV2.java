package com.alc.diary.application.calendar;

import com.alc.diary.application.calendar.dto.request.CreateCalendarRequestV2;
import com.alc.diary.application.calendar.dto.request.UpdateCalendarRequestV2;
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
import com.alc.diary.domain.calendar.repository.UserCalendarRepository;
import com.alc.diary.domain.categoryunit.CategoryUnit;
import com.alc.diary.domain.categoryunit.CategoryUnitRepository;
import com.alc.diary.domain.drink.Drink;
import com.alc.diary.domain.drink.repository.DrinkRepository;
import com.alc.diary.domain.drinkunit.DrinkUnit;
import com.alc.diary.domain.drinkunit.DrinkUnitRepository;
import com.alc.diary.domain.exception.DomainException;
import com.alc.diary.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class CalendarServiceV2 {

    private final UserRepository userRepository;
    private final CalendarRepository calendarRepository;
    private final DrinkRepository drinkRepository;
    private final UserCalendarRepository userCalendarRepository;
    private final DrinkUnitRepository drinkUnitRepository;
    private final CategoryUnitRepository categoryUnitRepository;

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
                    if (drink.getCategoryId() == 8) {
                        drinkType = DrinkType.BEER;
                    } else if (drink.getCategoryId() == 9) {
                        drinkType = DrinkType.SOJU;
                    } else if (drink.getCategoryId() == 10) {
                        drinkType = DrinkType.WINE;
                    } else if (drink.getCategoryId() == 11) {
                        drinkType = DrinkType.MAKGEOLLI;
                    } else {
                        drinkType = DrinkType.BEER;
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

                    Set<Long> availableUnitIds = categoryUnitRepository.findByCategoryId(drink.getCategoryId()).stream()
                            .map(categoryUnit -> categoryUnit.getUnit().getId())
                            .collect(Collectors.toSet());
                    if (!availableUnitIds.contains(drinkDto.drinkUnitId())) {
                        throw new DomainException(DrinkRecordError.INVALID_DRINK_UNIT);
                    }
                    return DrinkRecord.create(drinkType, drinkUnit, drinkDto.drinkId(), drinkDto.drinkUnitId(), drinkDto.quantity());
                })
                .toList();
    }

    public CalendarDto getCalendarById(long calendarId) {
        return calendarRepository.findById(calendarId)
                .map(CalendarDto::fromDomainModelDetail)
                .orElseThrow(() -> new DomainException(CalendarError.CALENDAR_NOT_FOUND));
    }

    @Transactional
    public void updateCalendar(long userId, long calendarId, UpdateCalendarRequestV2 request) {
        Calendar calendar = calendarRepository.findById(calendarId)
                .orElseThrow(() -> new DomainException(CalendarError.CALENDAR_NOT_FOUND));
        if (request.title() != null) {
            if (userId != calendar.getOwnerId()) {
                throw new DomainException(CalendarError.NO_PERMISSION);
            }
            calendar.updateTitle(request.title());
        }
        if (request.drinkDate() != null) {
            if (userId != calendar.getOwnerId()) {
                throw new DomainException(CalendarError.NO_PERMISSION);
            }
            calendar.updateDrinkDate(request.drinkDate());
        }
        if (!CollectionUtils.isEmpty(request.userCalendars())) {
            List<UpdateCalendarRequestV2.UserCalendarDto> create = request.userCalendars().stream()
                    .filter(userCalendarDto -> userCalendarDto.id() == null)
                    .toList();
            if (!create.isEmpty()) {
                if (userId != calendar.getOwnerId()) {
                    throw new DomainException(CalendarError.NO_PERMISSION);
                }

                List<UserCalendar> userCalendarsToSave = request.userCalendars().stream()
                        .map(userCalendarDto -> UserCalendar.createTaggedUserCalendar(userCalendarDto.userId()))
                        .toList();
                calendar.addUserCalendars(userCalendarsToSave);
            }
            Map<Long, UpdateCalendarRequestV2.UserCalendarDto> userCalendarsToUpdate = request.userCalendars().stream()
                    .filter(userCalendarDto -> userCalendarDto.id() != null)
                    .collect(Collectors.toMap(UpdateCalendarRequestV2.UserCalendarDto::id, it -> it));
            calendar.getUserCalendars().stream()
                    .filter(userCalendar -> userCalendarsToUpdate.containsKey(userCalendar.getId()))
                    .forEach(userCalendar -> {
                        UpdateCalendarRequestV2.UserCalendarDto userCalendarUpdateData = userCalendarsToUpdate.get(userCalendar.getId());

                        userCalendar.markAsRecorded();

                        if (userCalendarUpdateData.contentShouldBeUpdated()) {
                            userCalendar.updateContent(userCalendarUpdateData.content());
                        }
                        if (userCalendarUpdateData.drinkConditionShouldBeUpdated()) {
                            userCalendar.updateCondition(userCalendarUpdateData.drinkCondition());
                        }

                        if (!CollectionUtils.isEmpty(userCalendarUpdateData.drinkRecords())) {
                            List<UpdateCalendarRequestV2.UserCalendarDto.DrinkRecordDto> drinkRecordsToCreate = userCalendarUpdateData.drinkRecords().stream()
                                    .filter(drinkRecordDto -> drinkRecordDto.id() == null)
                                    .toList();
                            List<DrinkRecord> drinkRecordsToSave = drinkRecordsToCreate.stream()
                                    .map(drinkRecordDto -> DrinkRecord.create(drinkRecordDto.drinkId(), drinkRecordDto.drinkUnitId(), drinkRecordDto.quantity()))
                                    .toList();
                            userCalendar.addDrinkRecords(drinkRecordsToSave);

                            Map<Long, UpdateCalendarRequestV2.UserCalendarDto.DrinkRecordDto> drinkRecordsToUpdate = userCalendarUpdateData.drinkRecords().stream()
                                    .filter(drinkRecordDto -> drinkRecordDto.id() != null)
                                    .collect(Collectors.toMap(UpdateCalendarRequestV2.UserCalendarDto.DrinkRecordDto::id, it -> it));

                            userCalendar.getDrinkRecords().stream()
                                    .filter(drinkRecord -> drinkRecordsToUpdate.containsKey(drinkRecord.getId()))
                                    .forEach(drinkRecord -> {
                                        UpdateCalendarRequestV2.UserCalendarDto.DrinkRecordDto drinkRecordUpdateData = drinkRecordsToUpdate.get(drinkRecord.getId());
                                        if (drinkRecordUpdateData.drinkId() != null) {
                                            drinkRecord.updateDrinkId(drinkRecordUpdateData.drinkId());
                                        }
                                        if (drinkRecordUpdateData.drinkUnitId() != null) {
                                            drinkRecord.updateDrinkUnitId(drinkRecordUpdateData.drinkUnitId());
                                        }
                                        if (drinkRecordUpdateData.quantity() != null) {
                                            drinkRecord.updateQuantity(drinkRecordUpdateData.quantity());
                                        }
                                    });
                        }

                        userCalendar.deleteDrinkRecords(userId, userCalendarUpdateData.deleteDrinkRecords());
                    });
            if (!CollectionUtils.isEmpty(request.deleteUserCalendars())) {
                if (userId == calendar.getOwnerId()) {
                    calendar.deleteUserCalendars(userId, request.deleteUserCalendars());
                } else {
                    if (request.deleteUserCalendars().size() != 1) {
                        throw new DomainException(CalendarError.INVALID_REQUEST);
                    }
                    Long userCalendarIdToBeDeleted = request.deleteUserCalendars().get(0);
                    calendar.deleteUserCalendar(userId, userCalendarIdToBeDeleted);
                }
            }
        }

        if (!CollectionUtils.isEmpty(request.photos())) {
            List<Photo> photosToSave = request.photos().stream()
                    .map(photoDto -> Photo.create(userId, photoDto.url()))
                    .toList();
            calendar.addPhotos(photosToSave);
        }

        request.deletePhotos().forEach(photoId -> calendar.deletePhoto(userId, photoId));
    }

    public List<CalendarDto> getDailyCalendars(long userId, LocalDate date) {
        List<Calendar> calendars = calendarRepository.findAllUserCalendarsInCalendarsWithInRangeAndUserId(userId, date, date);
        return calendars.stream()
                .map(CalendarDto::fromDomainModelDetail)
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
