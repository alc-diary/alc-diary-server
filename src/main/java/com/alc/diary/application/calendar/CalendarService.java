package com.alc.diary.application.calendar;

import com.alc.diary.application.calendar.dto.request.CreateCalendarRequest;
import com.alc.diary.application.calendar.dto.response.CreateCalendarResponse;
import com.alc.diary.domain.calendar.*;
import com.alc.diary.domain.calendar.repository.CalendarRepository;
import com.alc.diary.domain.drink.repository.DrinkUnitInfoRepository;
import com.alc.diary.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class CalendarService {

    private final UserRepository userRepository;
    private final CalendarRepository calendarRepository;
    private final DrinkUnitInfoRepository drinkUnitInfoRepository;

//    /**
//     * 캘린더 생성
//     *
//     * @param userId
//     * @param request
//     * @return
//     */
//    @Transactional
//    public CreateCalendarResponse createCalendarLegacy(long userId, CreateCalendarRequest request) {
//        CalendarLegacy calendarLegacyToSave = createNewCalendar(userId, request);
//
//        CalendarLegacy calendarLegacy = calendarLegacyRepository.save(calendarLegacyToSave);
//
//        return new CreateCalendarResponse(
//                calendarLegacy.getId(),
//                calendarLegacy.getUserCalendarLagecies().stream()
//                        .map(UserCalendarLegacy::getId)
//                        .toList()
//        );
//    }

    @Transactional
    public CreateCalendarResponse createCalendar(long userId, CreateCalendarRequest request) {
        Calendar calendarToSave =
                Calendar.create(userId, request.title(), request.drinkStartTime(), request.drinkEndTime());
        UserCalendar userCalendarToSave = UserCalendar.create(userId, request.content(), request.drinkCondition());
        List<DrinkRecord> drinkRecordsToSave = request.drinks().stream()
                .map(drinkDto -> DrinkRecord.create(drinkDto.drinkType(), drinkDto.drinkUnit(), drinkDto.quantity()))
                .toList();
        userCalendarToSave.addDrinkRecords(drinkRecordsToSave);

        List<UserCalendar> taggedUserCalendarsToSave = request.taggedUserIds().stream()
                .map(UserCalendar::createTaggedUserCalendar)
                .toList();
        List<Photo> photosToSave = request.photos().stream()
                .map(photoDto -> Photo.create(userId, photoDto.url()))
                .toList();

        calendarToSave.addUserCalendar(userCalendarToSave);
        calendarToSave.addUserCalendars(taggedUserCalendarsToSave);
        calendarToSave.addPhotos(photosToSave);

        Calendar calendar = calendarRepository.save(calendarToSave);
        return new CreateCalendarResponse(
                calendar.getId(),
                calendar.getUserCalendars().stream()
                        .map(UserCalendar::getId)
                        .toList()
        );
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
//
//    /**
//     * 캘린더 상세 조회
//     *
//     * @param calendarId
//     * @return
//     */
//    public GetCalendarByIdResponse getCalendarByIdResponse(long userId, long calendarId) {
//        return calendarLegacyRepository.findById(calendarId)
//                .filter(calendar -> calendar.isInvolvedUser(userId))
//                .map(calendar -> GetCalendarByIdResponse.of(calendar, userId))
//                .orElseThrow(() -> new DomainException(CalendarError.CALENDAR_NOT_FOUND));
//    }
//
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
//    /**
//     * 해당 유저의 캘린더 조회(일별)
//     *
//     * @param userId
//     * @param date
//     * @return
//     */
//    public List<GetDailyCalendarsResponse> getDailyCalendars(long userId, LocalDate date, ZoneId zoneId) {
//        ZonedDateTime rangeStart = date.atStartOfDay(zoneId);
//        ZonedDateTime rangeEnd = date.plusDays(1).atStartOfDay(zoneId);
//        List<CalendarLegacy> calendarLagacies = calendarLegacyRepository.findCalendarsWithInRangeAndUserId(userId, rangeStart, rangeEnd);
//        Set<Long> userIds = calendarLagacies.stream()
//                .flatMap(calendar -> calendar.getUserCalendarsExceptByUserId(userId).stream())
//                .map(UserCalendarLegacy::getUserId)
//                .collect(Collectors.toSet());
//        Map<Long, User> userById = userRepository.findActiveUsersByIdIn(userIds).stream()
//                .collect(Collectors.toMap(User::getId, Function.identity()));
//        return calendarLagacies.stream()
//                .map(calendar -> GetDailyCalendarsResponse.of(calendar, userId, userById))
//                .toList();
//    }
//
//    /**
//     * 해당 유저의 캘린더 조회(월별)
//     *
//     * @param userId
//     * @param month
//     * @return
//     */
//    public List<GetMonthlyCalendarsResponse> getMonthlyCalendars(long userId, YearMonth month, ZoneId zoneId) {
//        ZonedDateTime rangeStart = month.atDay(1).atStartOfDay(zoneId);
//        ZonedDateTime rangeEnd = month.plusMonths(1).atDay(1).atStartOfDay(zoneId);
//        Calendars calendars = Calendars.from(calendarLegacyRepository.findCalendarsWithInRangeAndUserId(userId, rangeStart, rangeEnd));
//
//        return calendars.getMostAlcoholConsumedPerDay(userId).stream()
//                .map(calendar -> createResponseFromCalendar(calendar, userId))
//                .flatMap(Optional::stream)
//                .toList();
//    }
//
//    public Optional<GetMonthlyCalendarsResponse> createResponseFromCalendar(CalendarLegacy calendarLegacy, long userId) {
//        return calendarLegacy.getUserCalendarByUserId(userId)
//                .flatMap(UserCalendarLegacy::getMostConsumedDrink)
//                .map(UserCalendarDrink::getDrinkUnitInfoId)
//                .map(drinkUnitInfoId -> new GetMonthlyCalendarsResponse(calendarLegacy.getDate().toString(), drinkUnitInfoId));
//    }
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
//            calendarLegacy.updateCondition(userId, request.condition());
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
}
