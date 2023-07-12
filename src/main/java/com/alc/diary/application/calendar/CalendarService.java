package com.alc.diary.application.calendar;

import com.alc.diary.application.calendar.dto.request.CreateCalendarRequest;
import com.alc.diary.application.calendar.dto.request.CreateCommentRequest;
import com.alc.diary.application.calendar.dto.request.UpdateCalendarRequest;
import com.alc.diary.application.calendar.dto.response.CreateCalendarResponse;
import com.alc.diary.application.calendar.dto.response.GetCalendarByIdResponse;
import com.alc.diary.application.calendar.dto.response.GetDailyCalendarsResponse;
import com.alc.diary.application.calendar.dto.response.GetMonthlyCalendarsResponse;
import com.alc.diary.domain.calendar.*;
import com.alc.diary.domain.calendar.Calendar;
import com.alc.diary.domain.calendar.error.CalendarError;
import com.alc.diary.domain.calendar.repository.CalendarRepository;
import com.alc.diary.domain.calendar.repository.PhotoRepository;
import com.alc.diary.domain.calendar.vo.DrinkRecordUpdateVo;
import com.alc.diary.domain.exception.DomainException;
import com.alc.diary.domain.user.User;
import com.alc.diary.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class CalendarService {

    private final UserRepository userRepository;
    private final CalendarRepository calendarRepository;
    private final PhotoRepository photoRepository;

    @Transactional
    public CreateCalendarResponse createCalendarAndGenerateResponse(long userId, CreateCalendarRequest request) {
        validRequest(request);
        Calendar calendarToSave = createCalendar(userId, request);
        List<Photo> photosToSave = createPhotos(userId, request);
        calendarToSave.addPhotos(photosToSave);

        List<UserCalendar> userCalendarsToSave = createUserCalendars(request);
        calendarToSave.addUserCalendars(userCalendarsToSave);

        Calendar calendar = calendarRepository.save(calendarToSave);

        return CreateCalendarResponse.from(calendar);
    }

    private void validRequest(CreateCalendarRequest request) {
        List<Long> userIds = request.userCalendars().stream()
                .map(CreateCalendarRequest.UserCalendarCreationDto::userId)
                .toList();
        HashSet<Long> uniqueUserIds = new HashSet<>(userIds);

        if (userIds.size() != uniqueUserIds.size()) {
            throw new DomainException(CalendarError.DUPLICATE_USER_CALENDAR);
        }

        if (uniqueUserIds.size() != userRepository.findActiveUserIdsByIdIn(uniqueUserIds).size()) {
            throw new DomainException(CalendarError.DEACTIVATED_USER_INCLUDE);
        }

        if (!isHalfUnit(request.totalDrinkQuantity())) {
            throw new DomainException(CalendarError.INVALID_DRINK_QUANTITY_INCREMENT);
        }

        request.userCalendars().stream()
                .flatMap(it -> it.drinks().stream())
                .map(CreateCalendarRequest.DrinkCreationDto::quantity)
                .forEach(quantity -> {
                    if (!isHalfUnit(quantity)) {
                        throw new DomainException(CalendarError.INVALID_DRINK_QUANTITY_INCREMENT);
                    }
                });
    }

    private boolean isHalfUnit(float value) {
        float multiplied = value * 2;
        float epsilon = 0.0001f;

        return Math.abs(multiplied - Math.round(multiplied)) < epsilon;
    }

    @NotNull
    private static Calendar createCalendar(long userId, CreateCalendarRequest request) {
        return Calendar.create(userId, request.title(), request.totalDrinkQuantity(), request.drinkStartTime(), request.drinkEndTime());
    }

    @NotNull
    private static List<UserCalendar> createUserCalendars(CreateCalendarRequest request) {
        return request.userCalendars().stream()
                .map(userCalendarCreationDto -> {
                    UserCalendar userCalendarToSave = UserCalendar.create(
                            userCalendarCreationDto.userId(),
                            userCalendarCreationDto.content(),
                            userCalendarCreationDto.condition());
                    List<DrinkRecord> drinkRecordsToSave = createDrinkRecords(userCalendarCreationDto);
                    userCalendarToSave.addDrinkRecords(drinkRecordsToSave);
                    return userCalendarToSave;
                })
                .toList();
    }

    @NotNull
    private static List<DrinkRecord> createDrinkRecords(CreateCalendarRequest.UserCalendarCreationDto userCalendarCreationDto) {
        return userCalendarCreationDto.drinks().stream()
                .map(drinkCreationDto -> DrinkRecord.create(
                        drinkCreationDto.drinkType(),
                        drinkCreationDto.drinkUnit(),
                        drinkCreationDto.quantity()
                ))
                .toList();
    }

    @NotNull
    private static List<Photo> createPhotos(long userId, CreateCalendarRequest request) {
        return request.photos().stream()
                .map(photoDto -> Photo.create(userId, photoDto.url()))
                .toList();
    }

    public GetCalendarByIdResponse getCalendarById(long userId, long calendarId) {
        Calendar calendar = calendarRepository.findById(calendarId)
                .orElseThrow(() -> new DomainException(CalendarError.CALENDAR_NOT_FOUND));

        List<Long> userIds = calendar.getUserCalendars().stream()
                .map(UserCalendar::getUserId)
                .toList();
        Map<Long, User> userByUserId = userRepository.findActiveUsersByIdIn(userIds).stream()
                .collect(Collectors.toMap(User::getId, Function.identity()));

        return GetCalendarByIdResponse.of(userId, calendar, userByUserId);
    }

    @Transactional
    public void createComment(long userId, long calendarId, CreateCommentRequest request) {
        Calendar calendar = calendarRepository.findById(calendarId)
                .orElseThrow(() -> new DomainException(CalendarError.CALENDAR_NOT_FOUND));
        if (!calendar.hasPermission(userId)) {
            throw new DomainException(CalendarError.NO_PERMISSION);
        }
    }

//    /**
//     * 요청한 유저의 캘린더 데이터 삭제 (캘린더 삭제 x, 캘린더 데이터 중 자신의 데이터만 삭제)
//     *
//     * @param userId
//     * @param userCalendarId
//     */
//    @Transactional
//    public void deleteUserCalendar(long userId, long userCalendarId) {
//        UserCalendarLegacy userCalendarLegacy = userCalendarLegacyRepository.findByIdAndIsDeletedEqFalse(userCalendarId)
//                .orElseThrow(() -> new DomainException(UserCalendarImageError.IMAGE_LIMIT_EXCEEDED));
//        userCalendarLegacy.delete(userId);
//    }
//

    /**
     * 해당 유저의 캘린더 조회(일별)
     *
     * @param userId
     * @param date
     * @param zoneId
     * @return
     */
    public List<GetDailyCalendarsResponse> getDailyCalendars(long userId, LocalDate date, ZoneId zoneId) {
        ZonedDateTime rangeStart = date.atStartOfDay(zoneId);
        ZonedDateTime rangeEnd = date.plusDays(1).atStartOfDay(zoneId);
        List<Calendar> calendars = calendarRepository.findAllUserCalendarsInCalendarsWithInRangeAndUserId(userId, rangeStart, rangeEnd);
        Set<Long> userIds = calendars.stream()
                .flatMap(calendar -> calendar.findUserCalendarsExcludingUserId(userId).stream())
                .map(UserCalendar::getUserId)
                .collect(Collectors.toSet());
        Map<Long, User> userById = userRepository.findActiveUsersByIdIn(userIds).stream()
                .collect(Collectors.toMap(User::getId, Function.identity()));
        return GetDailyCalendarsResponse.of(userId, calendars, userById);
    }

    /**
     * 해당 유저의 캘린더 조회(월별)
     *
     * @param userId
     * @param month
     * @param zoneId
     * @return
     */
    public List<GetMonthlyCalendarsResponse> getMonthlyCalendars(long userId, YearMonth month, ZoneId zoneId) {
        ZonedDateTime rangeStart = month.atDay(1).atStartOfDay(zoneId);
        ZonedDateTime rangeEnd = month.plusMonths(1).atDay(1).atStartOfDay(zoneId);

        Calendars calendars =
                new Calendars(
                        calendarRepository.findAllUserCalendarsInCalendarsWithInRangeAndUserId(userId, rangeStart, rangeEnd),
                        zoneId
                );

        return calendars.getCalendarsByMaxDrinkPerDay(zoneId).stream()
                .map(calendar -> new GetMonthlyCalendarsResponse(
                        calendar.getDrinkStartTimeLocalDate(zoneId).toString(),
                        calendar.getMostConsumedDrinkType()
                ))
                .sorted(Comparator.comparing(GetMonthlyCalendarsResponse::date))
                .toList();
    }

    /**
     * 캘린더 데이터 수정
     *
     * @param userId
     * @param calendarId
     * @param userCalendarId
     * @param request
     */
    @Transactional
    public void updateCalendar(long userId, long calendarId, long userCalendarId, UpdateCalendarRequest request) {
        Calendar calendar =
                calendarRepository.findById(calendarId)
                        .orElseThrow(() -> new DomainException(CalendarError.CALENDAR_NOT_FOUND));

        calendar.updateTitle(userId, request.title());
        if (request.contentShouldBeUpdated()) {
            calendar.updateContent(userId, request.content());
        }

        if (request.conditionShouldBeUpdated()) {
            calendar.updateCondition(userId, request.condition());
        }

        calendar.updateDrinkStartTimeAndEndTime(userId, request.drinkStartTime(), request.drinkEndTime());

        calendar.updateDrinkRecords(userId, request.drinks().updated().stream().map(drinkRecordUpdateData ->
                        new DrinkRecordUpdateVo(
                                drinkRecordUpdateData.id(),
                                drinkRecordUpdateData.drinkType(),
                                drinkRecordUpdateData.drinkUnit(),
                                drinkRecordUpdateData.quantity()
                        ))
                .toList());
        calendar.deleteDrinkRecords(userId, request.drinks().deleted());

        List<DrinkRecord> drinkRecordsToSave = request.drinks().added().stream()
                .map(creationData -> DrinkRecord.create(
                        creationData.drinkType(),
                        creationData.drinkUnit(),
                        creationData.quantity()
                ))
                .toList();
        calendar.addDrinkRecords(userId, drinkRecordsToSave);

        List<Photo> photosToSave = request.photos().added().stream()
                .map(imageCreationData -> Photo.create(userId, imageCreationData.url()))
                .toList();
        calendar.addPhotos(photosToSave);

        List<Long> photoIdsToDelete = calendar.getPhotos().stream()
                .map(Photo::getId)
                .filter(photoId -> request.photos().deleted().contains(photoId))
                .toList();
        photoRepository.deleteByIdIn(photoIdsToDelete);
    }
}
