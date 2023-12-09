package com.alc.diary.application.calendar;

import com.alc.diary.application.calendar.dto.request.CreateCalendarFromMainRequest;
import com.alc.diary.application.calendar.dto.request.CreateCalendarFromMainRequest.DrinkCreationDto;
import com.alc.diary.application.calendar.dto.request.CreateCalendarRequest;
import com.alc.diary.application.calendar.dto.request.UpdateCalendarRequest;
import com.alc.diary.application.calendar.dto.request.UpdateUserCalendarRequest;
import com.alc.diary.application.calendar.dto.response.*;
import com.alc.diary.application.notification.NotificationService;
import com.alc.diary.domain.calendar.*;
import com.alc.diary.domain.calendar.Calendar;
import com.alc.diary.domain.calendar.error.CalendarError;
import com.alc.diary.domain.calendar.error.UserCalendarError;
import com.alc.diary.domain.calendar.repository.CalendarRepository;
import com.alc.diary.domain.calendar.repository.UserCalendarRepository;
import com.alc.diary.domain.calendar.vo.DrinkRecordUpdateVo;
import com.alc.diary.domain.calender.repository.CustomCalenderRepository;
import com.alc.diary.domain.exception.DomainException;
import com.alc.diary.domain.user.User;
import com.alc.diary.domain.user.error.UserError;
import com.alc.diary.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.*;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class CalendarService {

    private final UserRepository userRepository;
    private final CalendarRepository calendarRepository;
    private final UserCalendarRepository userCalendarRepository;
    private final CustomCalenderRepository customCalenderRepository;
    private final CacheManager cacheManager;
    private final NotificationService notificationService;

    /**
     * 캘린더 생성
     *
     * @param userId
     * @param request
     * @return
     */
    @Caching(evict = {
            @CacheEvict(value = "monthlyReport", key = "#userId + '_' + #request.drinkStartTime().year + '-' + #request.drinkStartTime().monthValue", cacheManager = "cacheManager"),
            @CacheEvict(value = "monthlyReport", key = "#userId + '_' + #request.drinkStartTime().plusMonths(1).year + '-' + #request.drinkStartTime().plusMonths(1).monthValue", cacheManager = "cacheManager")
    })
    @Transactional
    public CreateCalendarResponse createCalendarAndGenerateResponse(long userId, CreateCalendarRequest request) {
        validRequest(userId, request);
        Calendar calendarToSave = createCalendar(userId, request);
        List<Photo> photosToSave = createPhotos(userId, request);
        calendarToSave.addPhotos(photosToSave);

        List<UserCalendar> userCalendarsToSave = createUserCalendars(request);
        calendarToSave.addUserCalendars(userCalendarsToSave);

        Calendar calendar = calendarRepository.save(calendarToSave);

        User user = userRepository.findActiveUserById(userId).orElseThrow(() -> new DomainException(UserError.USER_NOT_FOUND));
        userCalendarsToSave.stream()
                .map(UserCalendar::getUserId)
                .filter(u -> u != userId)
                .forEach(taggedUserId -> notificationService.sendFcm(taggedUserId, "술렁술렁", "[" + user.getNickname() + "] 친구가 음주기록에 널 태그했어!\n어떤 기록인지 봐볼까?", "FRIEND_TAGGED"));

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
        Calendar calendar = getCalendarById(calendarId);

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

        Map<Long, User> userById = mapUsersByUserId(calendars);
        return GetDailyCalendarsResponse.of(userId, calendars, userById);
    }

    @NotNull
    private Map<Long, User> mapUsersByUserId(List<Calendar> calendars) {
        Set<Long> userIds = collectUserIdsFromCalendars(calendars);
        return userRepository.findActiveUsersByIdIn(userIds).stream()
                .collect(Collectors.toMap(User::getId, Function.identity()));
    }

    @NotNull
    private static Set<Long> collectUserIdsFromCalendars(List<Calendar> calendars) {
        return calendars.stream()
                .flatMap(calendar -> {
                    List<Long> ids = new ArrayList<>();
                    ids.add(calendar.getOwnerId());
                    ids.addAll(
                            calendar.getUserCalendars().stream()
                                    .map(UserCalendar::getUserId)
                                    .toList()
                    );
                    return ids.stream();
                })
                .collect(Collectors.toSet());
    }

    /**
     * 유저의 캘린더 삭제 (해당 유저의 데이터만 삭제, 캘린더 전체 삭제 x)
     *
     * @param userId
     * @param userCalendarId
     */
    @Transactional
    public void deleteUserCalendar(long userId, long calendarId, long userCalendarId) {
        UserCalendar userCalendar = getUserCalendarById(userCalendarId);
        if (!userCalendar.isOwner(userId)) {
            throw new DomainException(UserCalendarError.NO_PERMISSION_TO_DELETE);
        }

        Calendar calendar = calendarRepository.findById(calendarId)
                .orElseThrow(() -> new DomainException(CalendarError.CALENDAR_NOT_FOUND));
        calendar.deleteUserCalendar(userCalendarId);
        evictMonthlyReportCacheForCurrentAndNextMonth(userId, calendar.getDrinkStartTime());
    }

    private void evictMonthlyReportCacheForCurrentAndNextMonth(long userId, ZonedDateTime drinkStartTime) {
        ZonedDateTime drinkStartTimePlusOneMonth = drinkStartTime.plusMonths(1);
        if (cacheManager.getCache("monthlyReport") != null) {
            cacheManager.getCache("monthlyReport").evict(userId + "_" + drinkStartTime.getYear() + "-" + drinkStartTime.getMonthValue());
            cacheManager.getCache("monthlyReport").evict(userId + "_" + drinkStartTimePlusOneMonth.getYear() + "-" + drinkStartTimePlusOneMonth.getMonthValue());
        }
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
        return GetMonthlyCalendarsResponse.fromDomainEntity(calendars, userId);
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
        Calendar calendar = getCalendarById(calendarId);
        checkOwnership(userId, calendar);

        updateCalendarDetails(userId, request, calendar);

        List<UserCalendar> taggedUserCalendars = calendar.getTaggedUserCalendars();
        Set<Long> taggedUserCalendarIds = extractUserIds(taggedUserCalendars);

        addUserCalendars(userId, request, taggedUserCalendarIds, calendar);

        deleteUserCalendars(request, taggedUserCalendars, calendar);
    }

    private static void checkOwnership(long userId, Calendar calendar) {
        if (!calendar.isOwner(userId)) {
            throw new DomainException(CalendarError.NO_PERMISSION);
        }
    }

    @NotNull
    private static Set<Long> extractUserIds(List<UserCalendar> taggedUserCalendars) {
        return taggedUserCalendars.stream()
                .map(UserCalendar::getUserId)
                .collect(Collectors.toSet());
    }

    private void updateCalendarDetails(long userId, UpdateCalendarRequest request, Calendar calendar) {
        ZonedDateTime now = ZonedDateTime.now();
        calendar.update(request.newTitle(), request.newDrinkStartTime(), request.newDrinkEndTime(), now);
        evictMonthlyReportCacheForCurrentAndNextMonth(userId, calendar.getDrinkStartTime());
        if (!calendar.getDrinkStartTime().equals(request.newDrinkStartTime())) {
            evictMonthlyReportCacheForCurrentAndNextMonth(userId, request.newDrinkStartTime());
        }
    }

    private void addUserCalendars(long userId, UpdateCalendarRequest request, Set<Long> taggedUserCalendarIds, Calendar calendar) {
        List<Long> addedTaggedUserIds = request.newTaggedUserIds().stream()
                .filter(taggedUserId -> !taggedUserCalendarIds.contains(taggedUserId))
                .toList();

        List<UserCalendar> userCalendarsToSave = addedTaggedUserIds.stream()
                .map(UserCalendar::createTaggedUserCalendar)
                .toList();
        calendar.addUserCalendars(userCalendarsToSave);

        User user = userRepository.findActiveUserById(userId)
                .orElseThrow(() -> new DomainException(UserError.USER_NOT_FOUND));

        addedTaggedUserIds.stream()
                .forEach(taggedUserId -> notificationService.sendFcm(taggedUserId, "술렁술렁", "[" + user.getNickname() + "] 친구가 음주기록에 널 태그했어!\n어떤 기록인지 봐볼까?", "FRIEND_TAGGED"));
    }

    private static void deleteUserCalendars(UpdateCalendarRequest request, List<UserCalendar> taggedUserCalendars, Calendar calendar) {
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
        UserCalendar userCalendar = getUserCalendarById(userCalendarId);
        validateUserCalendarOwnership(userId, userCalendar);

        Calendar calendar = getCalendarById(calendarId);

        evictMonthlyReportCacheForCurrentAndNextMonth(userId, calendar.getDrinkStartTime());

        updateCalendarContentAndCondition(userCalendarId, request, calendar);
        updateDrinkRecords(userId, request, userCalendar);
        updatePhotos(userId, request, calendar);

        userCalendar.markAsRecorded();
    }

    private UserCalendar getUserCalendarById(long userCalendarId) {
        return userCalendarRepository.findById(userCalendarId)
                .orElseThrow(() -> new DomainException(UserCalendarError.USER_CALENDAR_NOT_FOUND));
    }

    private static void validateUserCalendarOwnership(long userId, UserCalendar userCalendar) {
        if (!userCalendar.isOwner(userId)) {
            throw new DomainException(UserCalendarError.NO_PERMISSION_TO_UPDATE);
        }
    }

    private static void updateCalendarContentAndCondition(long userCalendarId, UpdateUserCalendarRequest request, Calendar calendar) {
        if (request.contentShouldBeUpdated()) {
            calendar.updateContent(userCalendarId, request.content());
        }
        if (request.conditionShouldBeUpdated()) {
            calendar.updateCondition(userCalendarId, request.condition());
        }
    }

    private static void updateDrinkRecords(long userId, UpdateUserCalendarRequest request, UserCalendar userCalendar) {
        List<DrinkRecord> drinkRecordsToSave = request.drinks().added().stream()
                .map(it -> DrinkRecord.create(it.drinkType(), it.drinkUnit(), it.quantity()))
                .toList();
        userCalendar.addDrinkRecords(drinkRecordsToSave);

        for (UpdateUserCalendarRequest.DrinkRecordUpdateData drinkRecordUpdateData : request.drinks().updated()) {
            updateExistingDrinkRecord(userCalendar, drinkRecordUpdateData);
        }

        userCalendar.deleteDrinkRecords(userId, request.drinks().deleted());
    }

    private static void updateExistingDrinkRecord(UserCalendar userCalendar, UpdateUserCalendarRequest.DrinkRecordUpdateData drinkRecordUpdateData) {
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

    private static void updatePhotos(long userId, UpdateUserCalendarRequest request, Calendar calendar) {
        for (Long photoId : request.photos().deleted()) {
            calendar.getPhotos().stream()
                    .filter(photo -> photo.getId().equals(photoId))
                    .findFirst()
                    .filter(photo -> photo.canBeDeletedBy(userId))
                    .ifPresent(Photo::delete);
        }

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
    @Caching(evict = {
            @CacheEvict(value = "monthlyReport", key = "#userId + '_' + #request.drinkStartTime().year + '-' + #request.drinkStartTime().monthValue", cacheManager = "cacheManager"),
            @CacheEvict(value = "monthlyReport", key = "#userId + '_' + #request.drinkStartTime().plusMonths(1).year + '-' + #request.drinkStartTime().plusMonths(1).monthValue", cacheManager = "cacheManager")
    })
    @Transactional
    public long createCalendarFromMain(long userId, CreateCalendarFromMainRequest request) {
        validateDrinkQuantity(request);

        float totalDrinkQuantity = calculateTotalDrinkQuantity(request);
        Calendar calendarToSave = Calendar.createForMain(userId, totalDrinkQuantity, request.drinkStartTime(), request.drinkEndTime());

        UserCalendar userCalendarToSave = createUserCalendar(userId, request);
        calendarToSave.addUserCalendar(userCalendarToSave);

        Calendar calendar = calendarRepository.save(calendarToSave);
        return calendar.getId();
    }

    private void validateDrinkQuantity(CreateCalendarFromMainRequest request) {
        request.drinks().forEach(dto -> {
            if (!isHalfUnit(dto.quantity())) {
                throw new DomainException(CalendarError.INVALID_DRINK_QUANTITY_INCREMENT);
            }
        });
    }

    private static float calculateTotalDrinkQuantity(CreateCalendarFromMainRequest request) {
        return (float) request.drinks().stream()
                .mapToDouble(DrinkCreationDto::quantity)
                .sum();
    }

    @NotNull
    private static UserCalendar createUserCalendar(long userId, CreateCalendarFromMainRequest request) {
        UserCalendar userCalendarToSave = UserCalendar.create(userId, null, null);
        List<DrinkRecord> drinkRecordsToSave = request.drinks().stream()
                .map(dto -> DrinkRecord.create(
                        dto.drinkType(),
                        dto.drinkUnit(),
                        dto.quantity()
                ))
                .toList();
        userCalendarToSave.addDrinkRecords(drinkRecordsToSave);
        return userCalendarToSave;
    }

    /**
     * 메인 페이지 조회시 사용하는 부가정보 조회
     *
     * @param userId
     * @return
     */
    public GetMainResponse getMain(long userId) {
        User user = userRepository.findActiveUserById(userId).orElseThrow(() -> new DomainException(UserError.USER_NOT_FOUND));
        long overAlcoholLimit = customCalenderRepository.countAlcoholLimitV2(userId);
        return GetMainResponse.of(user, overAlcoholLimit);
    }

    private Calendar getCalendarById(long calendarId) {
        return calendarRepository.findById(calendarId)
                .orElseThrow(() -> new DomainException(CalendarError.CALENDAR_NOT_FOUND));
    }
}
