package com.alc.diary.application.calendar;

import com.alc.diary.application.calendar.dto.request.CreateCalendarFromMainRequest;
import com.alc.diary.application.calendar.dto.request.CreateCalendarFromMainRequest.DrinkCreationDto;
import com.alc.diary.application.calendar.dto.request.CreateCalendarRequest;
import com.alc.diary.application.calendar.dto.request.UpdateCalendarRequest;
import com.alc.diary.application.calendar.dto.request.UpdateUserCalendarRequest;
import com.alc.diary.application.calendar.dto.response.*;
import com.alc.diary.domain.calendar.*;
import com.alc.diary.domain.calendar.Calendar;
import com.alc.diary.domain.calendar.error.CalendarError;
import com.alc.diary.domain.calendar.error.UserCalendarError;
import com.alc.diary.domain.calendar.repository.CalendarRepository;
import com.alc.diary.domain.calendar.repository.PhotoRepository;
import com.alc.diary.domain.calendar.repository.UserCalendarRepository;
import com.alc.diary.domain.calendar.vo.DrinkRecordUpdateVo;
import com.alc.diary.domain.calender.repository.CustomCalenderRepository;
import com.alc.diary.domain.exception.DomainException;
import com.alc.diary.domain.user.User;
import com.alc.diary.domain.user.error.UserError;
import com.alc.diary.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.*;
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
    private final UserCalendarRepository userCalendarRepository;
    private final CustomCalenderRepository customCalenderRepository;

    /**
     * 캘린더 생성
     *
     * @param userId
     * @param request
     * @return
     */
    @Transactional
    public CreateCalendarResponse createCalendarAndGenerateResponse(long userId, CreateCalendarRequest request) {
        validRequest(userId, request);
        Calendar calendarToSave = createCalendar(userId, request);
        List<Photo> photosToSave = createPhotos(userId, request);
        calendarToSave.addPhotos(photosToSave);

        List<UserCalendar> userCalendarsToSave = createUserCalendars(request);
        calendarToSave.addUserCalendars(userCalendarsToSave);

        Calendar calendar = calendarRepository.save(calendarToSave);

        return CreateCalendarResponse.from(calendar);
    }

    private void validRequest(long userId, CreateCalendarRequest request) {
        if (userId != request.userCalendar().userId()) {
            throw new DomainException(CalendarError.INVALID_REQUEST);
        }

        boolean allValid = request.userCalendar().drinks().stream()
                .map(CreateCalendarRequest.DrinkCreationDto::quantity)
                .allMatch(CalendarService::isHalfUnit);
        if (!allValid) {
            throw new DomainException(CalendarError.INVALID_DRINK_QUANTITY_INCREMENT);
        }
    }

    private static boolean isHalfUnit(float value) {
        float multiplied = value * 2;
        float epsilon = 0.0001f;

        return Math.abs(multiplied - Math.round(multiplied)) < epsilon;
    }

    @NotNull
    private static Calendar createCalendar(long userId, CreateCalendarRequest request) {
        float totalDrinkQuantity = (float) request.userCalendar().drinks().stream()
                .mapToDouble(CreateCalendarRequest.DrinkCreationDto::quantity)
                .sum();
        return Calendar.create(
                userId,
                request.title(),
                totalDrinkQuantity,
                request.drinkStartTime(),
                request.drinkEndTime()
        );
    }

    @NotNull
    private List<UserCalendar> createUserCalendars(CreateCalendarRequest request) {
        List<UserCalendar> userCalendars = new ArrayList<>();

        UserCalendar userCalendar = UserCalendar.create(request.userCalendar().userId(), request.userCalendar().content(), request.userCalendar().condition());
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
        if (CollectionUtils.size(request.photos()) > 20) {
            throw new DomainException(CalendarError.IMAGE_LIMIT_EXCEEDED);
        }
        return request.photos().stream()
                .map(photoDto -> Photo.create(userId, photoDto.url()))
                .toList();
    }

    /**
     * 캘린더 상세 조회
     *
     * @param userId
     * @param calendarId
     * @return
     */
    public GetCalendarByIdResponse getCalendarById(long userId, long calendarId) {
        Calendar calendar = calendarRepository.findById(calendarId)
                .orElseThrow(() -> new DomainException(CalendarError.CALENDAR_NOT_FOUND));
        // calendarRepository.findWithPhotoById(userId);

        List<Long> userIds = calendar.getUserCalendars().stream()
                .map(UserCalendar::getUserId)
                .toList();
        Map<Long, User> userByUserId = userRepository.findActiveUsersByIdIn(userIds).stream()
                .collect(Collectors.toMap(User::getId, Function.identity()));

        return GetCalendarByIdResponse.of(userId, calendar, userByUserId);
    }

    /**
     * 해당 유저의 캘린더 조회 (일별)
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
     * 유저의 캘린더 삭제 (해당 유저의 데이터만 삭제, 캘린더 전체 삭제 x)
     *
     * @param userId
     * @param userCalendarId
     */
    @Transactional
    public void deleteUserCalendar(long userId, long calendarId, long userCalendarId) {
        UserCalendar userCalendar = userCalendarRepository.findById(userCalendarId)
                .orElseThrow(() -> new DomainException(UserCalendarError.USER_CALENDAR_NOT_FOUND));
        if (!userCalendar.isOwner(userId)) {
            throw new DomainException(UserCalendarError.NO_PERMISSION_TO_DELETE);
        }

        Calendar calendar = calendarRepository.findById(calendarId)
                .orElseThrow(() -> new DomainException(CalendarError.CALENDAR_NOT_FOUND));
        calendar.deleteUserCalendar(userCalendarId);
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
                        calendar.getMostConsumedDrinkType(),
                        true
                ))
                .sorted(Comparator.comparing(GetMonthlyCalendarsResponse::date))
                .toList();
    }

    /**
     * 캘린더 수정
     *
     * @param userId
     * @param calendarId
     * @param request
     */
    @Transactional
    public void updateCalendar(long userId, long calendarId, UpdateCalendarRequest request) {
        Calendar calendar = calendarRepository.findById(calendarId)
                .orElseThrow(() -> new DomainException(CalendarError.CALENDAR_NOT_FOUND));
        if (!calendar.isOwner(userId)) {
            throw new DomainException(CalendarError.NO_PERMISSION);
        }
        ZonedDateTime now = ZonedDateTime.now();
        calendar.update(request.newTitle(), request.newDrinkStartTime(), request.newDrinkEndTime(), now);

        // Update UserCalendars
        List<UserCalendar> taggedUserCalendars = calendar.getTaggedUserCalendars();
        Set<Long> taggedUserCalendarIds = taggedUserCalendars.stream()
                .map(UserCalendar::getUserId)
                .collect(Collectors.toSet());

        // 1. Add new UserCalendars
        List<Long> addedTaggedUserIds = request.newTaggedUserIds().stream()
                .filter(taggedUserId -> !taggedUserCalendarIds.contains(taggedUserId))
                .toList();
        List<UserCalendar> userCalendarsToSave = addedTaggedUserIds.stream()
                .map(UserCalendar::createTaggedUserCalendar)
                .toList();
        calendar.addUserCalendars(userCalendarsToSave);

        // 2. Delete UserCalendars
        List<Long> userCalendarIdsToDelete = taggedUserCalendars.stream()
                .filter(userCalendar -> !request.newTaggedUserIds().contains(userCalendar.getUserId()))
                .map(UserCalendar::getId)
                .toList();
        calendar.deleteUserCalendars(userCalendarIdsToDelete);
    }

    /**
     * 유저 캘린더 데이터 수정
     *
     * @param userId
     * @param calendarId
     * @param userCalendarId
     * @param request
     */
    @Transactional
    public void updateUserCalendar(long userId, long calendarId, long userCalendarId, UpdateUserCalendarRequest request) {
        UserCalendar userCalendar = userCalendarRepository.findById(userCalendarId)
                .orElseThrow(() -> new DomainException(UserCalendarError.USER_CALENDAR_NOT_FOUND));
        if (!userCalendar.isOwner(userId)) {
            throw new DomainException(UserCalendarError.NO_PERMISSION_TO_UPDATE);
        }

        Calendar calendar = calendarRepository.findById(calendarId)
                .orElseThrow(() -> new DomainException(CalendarError.CALENDAR_NOT_FOUND));

        if (request.contentShouldBeUpdated()) {
            calendar.updateContent(userCalendarId, request.content());
        }
        if (request.conditionShouldBeUpdated()) {
            calendar.updateCondition(userCalendarId, request.condition());
        }

        List<DrinkRecord> drinkRecordsToSave = request.drinks().added().stream()
                .map(it -> DrinkRecord.create(it.drinkType(), it.drinkUnit(), it.quantity()))
                .toList();
        userCalendar.addDrinkRecords(drinkRecordsToSave);

        for (UpdateUserCalendarRequest.DrinkRecordUpdateData drinkRecordUpdateData : request.drinks().updated()) {
            userCalendar.getDrinkRecords().stream()
                    .filter(it -> it.getId().equals(drinkRecordUpdateData.id()))
                    .findFirst()
                    .ifPresent(it -> it.updateRecord(new DrinkRecordUpdateVo(
                            drinkRecordUpdateData.id(),
                            drinkRecordUpdateData.drinkType(),
                            drinkRecordUpdateData.drinkUnit(),
                            drinkRecordUpdateData.quantity()
                            )));
        }

        userCalendar.deleteDrinkRecords(userId, request.drinks().deleted());

        userCalendar.markAsRecorded();

        // 사진 삭제
        for (Long photoId : request.photos().deleted()) {
            calendar.getPhotos().stream()
                    .filter(photo -> photo.getId().equals(photoId))
                    .findFirst()
                    .filter(photo -> photo.canBeDeletedBy(userId))
                    .ifPresent(Photo::delete);
        }

        // 사진 추가
        List<Photo> photosToSave = request.photos().added().stream()
                .map(it -> Photo.create(userId, it.url()))
                .toList();
        calendar.addPhotos(photosToSave);
    }

    /**
     * 메인에서 캘린더 생성
     *
     * @param userId
     * @param request
     */
    @Transactional
    public long createCalendarFromMain(long userId, CreateCalendarFromMainRequest request) {
        float totalDrinkQuantity = (float) request.drinks().stream()
                .mapToDouble(DrinkCreationDto::quantity)
                .sum();
        Calendar calendarToSave = Calendar.create(userId, "오늘의 음주기록", totalDrinkQuantity, request.drinkStartTime(), request.drinkEndTime());
        UserCalendar userCalendarToSave = UserCalendar.create(userId, null, null);
        calendarToSave.addUserCalendar(userCalendarToSave);

        Calendar calendar = calendarRepository.save(calendarToSave);
        return calendar.getId();
    }

    public GetMainResponse getMain(long userId) {
        User user = userRepository.findActiveUserById(userId).orElseThrow(() -> new DomainException(UserError.USER_NOT_FOUND));
        long overAlcoholLimit = customCalenderRepository.countAlcoholLimitV2(userId);
        return GetMainResponse.create(
                user.getDetail().getNickname(),
                user.getDetail().getDescriptionStyle(),
                overAlcoholLimit,
                user.getDetail().getNonAlcoholGoal()
        );
    }
}
