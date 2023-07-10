package com.alc.diary.application.calendar;

import com.alc.diary.application.calendar.dto.request.CreateCalendarRequest;
import com.alc.diary.application.calendar.dto.request.CreateCommentRequest;
import com.alc.diary.application.calendar.dto.request.UpdateCalendarRequest;
import com.alc.diary.application.calendar.dto.response.CreateCalendarResponse;
import com.alc.diary.application.calendar.dto.response.GetCalendarByIdResponse;
import com.alc.diary.application.calendar.dto.response.GetDailyCalendarsResponse;
import com.alc.diary.application.calendar.dto.response.GetMonthlyCalendarsResponse;
import com.alc.diary.domain.calendar.*;
import com.alc.diary.domain.calendar.error.CalendarError;
import com.alc.diary.domain.calendar.repository.CalendarRepository;
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
import java.util.Comparator;
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

    @Transactional
    public CreateCalendarResponse createCalendarAndGenerateResponse(long userId, CreateCalendarRequest request) {
        Calendar calendarToSave = createCalendar(userId, request);
        UserCalendar userCalendarToSave = createUserCalendar(userId, request);
        List<DrinkRecord> drinkRecordsToSave = createDrinkRecords(request);
        userCalendarToSave.addDrinkRecords(drinkRecordsToSave);

        List<UserCalendar> taggedUserCalendarsToSave = createTaggedUserCalendars(userId, request);
        List<Photo> photosToSave = createPhotos(userId, request);

        calendarToSave.addUserCalendar(userCalendarToSave);
        calendarToSave.addUserCalendars(taggedUserCalendarsToSave);
        calendarToSave.addPhotos(photosToSave);
        Calendar calendar = calendarRepository.save(calendarToSave);

        return CreateCalendarResponse.from(calendar);
    }

    @NotNull
    Calendar createCalendar(long userId, CreateCalendarRequest request) {
        return Calendar.create(userId, request.title(), request.drinkStartTime(), request.drinkEndTime());
    }

    @NotNull
    private static UserCalendar createUserCalendar(long userId, CreateCalendarRequest request) {
        return UserCalendar.create(userId, request.content(), request.drinkCondition());
    }

    @NotNull
    private static List<DrinkRecord> createDrinkRecords(CreateCalendarRequest request) {
        return request.drinks().stream()
                .map(drinkDto -> DrinkRecord.create(drinkDto.drinkType(), drinkDto.drinkUnit(), drinkDto.quantity()))
                .toList();
    }

    @NotNull
    private List<UserCalendar> createTaggedUserCalendars(long userId, CreateCalendarRequest request) {
        Set<Long> activeTaggedUserIds = userRepository.findActiveUserIdsByIdIn(
                request.taggedUserIds().stream()
                        .filter(taggedUserId -> taggedUserId != userId)
                        .toList()
        );

        return request.taggedUserIds().stream()
                .filter(activeTaggedUserIds::contains)
                .map(UserCalendar::createTaggedUserCalendar)
                .toList();
    }

    @NotNull
    private static List<Photo> createPhotos(long userId, CreateCalendarRequest request) {
        return request.photos().stream()
                .map(photoDto -> Photo.create(userId, photoDto.url()))
                .toList();
    }
//
//    @NotNull
//    private CalendarLegacy createNewCalendar(long userId, CreateCalendarRequest request) {
//        CalendarLegacy calendarLegacyToSave =
//                CalendarLegacy.create(userId, request.title(), request.drinkStartTime(), request.drinkEndTime());
//
//        UserCalendarLegacy userCalendarLegacyToSave = createUserCalendar(userId, request);
//        List<UserCalendarLegacy> taggedUsersCalendarsToSave = createUserCalendarsForTaggedUsers(userId, request);
//
//        calendarLegacyToSave.addUserCalendar(userCalendarLegacyToSave);
//        calendarLegacyToSave.addUserCalendars(taggedUsersCalendarsToSave);
//
//        return calendarLegacyToSave;
//    }
//
//    @NotNull
//    private UserCalendarLegacy createUserCalendar(long userId, CreateCalendarRequest request) {
//        UserCalendarLegacy userCalendarLegacyToSave = UserCalendarLegacy.create(userId, request.content(), request.drinkCondition());
//
//        List<Long> drinkUnitInfoIds = request.userCalendarDrinks().stream()
//                .map(CreateCalendarRequest.DrinkDto::drinkUnitInfoId)
//                .toList();
//        List<UserCalendarDrink> userCalendarDrinksToSave =
//                createUserCalendarDrinks(drinkUnitInfoIds, request.userCalendarDrinks());
//        List<UserCalendarImage> userCalendarImagesToSave = createUserCalendarImages(request.userCalendarImages());
//        userCalendarLegacyToSave.addImages(userCalendarImagesToSave);
//        userCalendarLegacyToSave.addDrinks(userCalendarDrinksToSave);
//
//        return userCalendarLegacyToSave;
//    }
//
//    @NotNull
//    private List<UserCalendarDrink> createUserCalendarDrinks(
//            List<Long> drinkUnitInfoIds,
//            List<CreateCalendarRequest.DrinkDto> drinkDtos
//    ) {
//        Map<Long, DrinkUnitInfo> drinkUnitInfoById = drinkUnitInfoRepository.findByIdIn(drinkUnitInfoIds).stream()
//                .collect(Collectors.toMap(DrinkUnitInfo::getId, Function.identity()));
//        return drinkDtos.stream()
//                .map(dto -> {
//                    DrinkUnitInfo drinkUnitInfo = drinkUnitInfoById.get(dto.drinkUnitInfoId());
//                    return new UserCalendarDrink(
//                            drinkUnitInfo.getId(),
//                            drinkUnitInfo.getPrice(),
//                            drinkUnitInfo.getCalories(),
//                            dto.quantity()
//                    );
//                })
//                .toList();
//    }
//
//    @NotNull
//    private static List<UserCalendarImage> createUserCalendarImages(List<CreateCalendarRequest.UserCalendarImageDto> userCalendarImageDtos) {
//        return userCalendarImageDtos.stream()
//                .map(dto -> new UserCalendarImage(dto.imageUrl()))
//                .toList();
//    }
//
//    @NotNull
//    private List<UserCalendarLegacy> createUserCalendarsForTaggedUsers(long userId, CreateCalendarRequest request) {
//        Set<Long> activeUserIds = userRepository.findActiveUserIdsByIdIn(request.taggedUserIds());
//        return request.taggedUserIds().stream()
//                .filter(taggedUserId -> taggedUserId != userId)
//                .filter(activeUserIds::contains)
//                .map(UserCalendarLegacy::createForTaggedUser)
//                .toList();
//    }

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

//
//    /**
//     * 캘린더 데이터 수정
//     *
//     * @param userId
//     * @param calendarId
//     * @param userCalendarId
//     * @param request
//     */
//    @Transactional
//    public void updateCalendar(long userId, long calendarId, long userCalendarId, UpdateCalendarRequest request) {
//        CalendarLegacy calendarLegacy = calendarLegacyRepository.findByIdAndUserCalendarId(calendarId, userCalendarId)
//                .orElseThrow(() -> new DomainException(CalendarError.CALENDAR_NOT_FOUND));
//
//        if (calendarLegacy.getUserCalendarByUserId(userId).isEmpty()) {
//            throw new DomainException(UserCalendarError.USER_CALENDAR_NOT_FOUND);
//        }
//
//        if (request.title() != null) {
//            if (!calendarLegacy.isOwner(userId)) {
//                throw new DomainException(CalendarError.NO_PERMISSION);
//            }
//            calendarLegacy.updateTitle(userId, request.title());
//        }
//
//        if (request.contentShouldBeUpdated()) {
//            calendarLegacy.updateContent(userId, request.content());
//        }
//
//        if (request.conditionShouldBeUpdated()) {
//            calendarLegacy.updateCondition(userId, request.drinkCondition());
//        }
//
//        if (request.drinkStartTime() != null) {
//            if (!calendarLegacy.isOwner(userId)) {
//                throw new DomainException(CalendarError.NO_PERMISSION);
//            }
//            calendarLegacy.updateDrinkStartTime(userId, request.drinkStartTime());
//        }
//        if (request.drinkEndTime() != null) {
//            if (!calendarLegacy.isOwner(userId)) {
//                throw new DomainException(CalendarError.NO_PERMISSION);
//            }
//            calendarLegacy.updateDrinkEndTime(userId, request.drinkEndTime());
//        }
//
//        request.drinks().updated().forEach(drinkUpdateData ->
//                calendarLegacy.updateUserCalendarDrink(userCalendarId, drinkUpdateData.id(), drinkUpdateData.quantity()));
//
//        calendarLegacy.removeDrinkByIds(userId, request.drinks().deleted());
//        calendarLegacy.removeImagesByIds(userId, request.images().deleted());
//
//    }

    @Transactional
    public void updateUserCalendar(long userId, long calendarId, long userCalendarId, UpdateCalendarRequest request) {
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

        calendar.
    }
}
