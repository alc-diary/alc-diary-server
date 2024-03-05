package com.alc.diary.domain.calendar;

import com.alc.diary.domain.BaseEntity;
import com.alc.diary.domain.calendar.enums.DrinkType;
import com.alc.diary.domain.calendar.error.CalendarError;
import com.alc.diary.domain.calendar.error.PhotoError;
import com.alc.diary.domain.calendar.error.UserCalendarError;
import com.alc.diary.domain.calendar.vo.DrinkRecordUpdateVo;
import com.alc.diary.domain.exception.DomainException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "calendars")
@Entity
public class Calendar extends BaseEntity {

    private static final int MAX_PHOTO_COUNT = 20;
    private static final String MAIN_CALENDAR_DEFAULT_TITLE = "오늘의 음주기록";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Audited
    @Column(name = "owner_id")
    private long ownerId;

    @Audited
    @Column(name = "title", length = 100, nullable = false)
    private String title;

    @Audited
    @Column(name = "total_drink_quantity", nullable = false)
    private float totalDrinkQuantity;

    @Audited
    @Column(name = "drink_start_time", nullable = false)
    private ZonedDateTime drinkStartTime;

    @Audited
    @Column(name = "drink_end_time", nullable = false)
    private ZonedDateTime drinkEndTime;

    @Audited
    @Column(name = "drink_date", length = 20)
    private LocalDate drinkDate;

    @OneToMany(mappedBy = "calendar", cascade = CascadeType.PERSIST)
    private List<UserCalendar> userCalendars = new ArrayList<>();

    @OneToMany(mappedBy = "calendar", cascade = CascadeType.PERSIST)
    private List<Photo> photos = new ArrayList<>();

    // @OneToMany(mappedBy = "calendar", cascade = CascadeType.PERSIST)
    // private List<Comment> comments = new ArrayList<>();

    @Audited
    @Column(name = "deletedAt")
    private LocalDateTime deletedAt;

    private Calendar(
            long ownerId,
            String title,
            float totalDrinkQuantity,
            ZonedDateTime drinkStartTime,
            ZonedDateTime drinkEndTime,
            LocalDate drinkDate,
            LocalDateTime deletedAt,
            ZonedDateTime now
    ) {
        validateTitle(title);
        if (drinkStartTime == null) {
            throw new DomainException(CalendarError.NULL_DRINK_START_TIME);
        }
        if (drinkEndTime == null) {
            throw new DomainException(CalendarError.NULL_DRINK_END_TIME);
        }
        if (drinkStartTime.isAfter(drinkEndTime)) {
            throw new DomainException(CalendarError.START_TIME_AFTER_END_TIME);
        }
        if (drinkEndTime.isAfter(now)) {
            throw new DomainException(CalendarError.END_TIME_IN_FUTURE);
        }
        this.ownerId = ownerId;
        this.title = title;
        this.totalDrinkQuantity = totalDrinkQuantity;
        this.drinkStartTime = drinkStartTime;
        this.drinkEndTime = drinkEndTime;
        this.drinkDate = drinkDate;
        this.deletedAt = deletedAt;
    }

    public static Calendar create(
            long ownerId,
            String title,
            float totalDrinkQuantity,
            ZonedDateTime drinkStartTime,
            ZonedDateTime drinkEndTime
    ) {
        return new Calendar(
                ownerId,
                title,
                totalDrinkQuantity,
                drinkStartTime,
                drinkEndTime,
                drinkStartTime.toLocalDate(),
                null,
                ZonedDateTime.now());
    }

    public static Calendar create(
            long ownerId,
            String title,
            float totalDrinkQuantity,
            ZonedDateTime drinkStartTime,
            ZonedDateTime drinkEndTime,
            ZonedDateTime now
    ) {
        return new Calendar(
                ownerId,
                title,
                totalDrinkQuantity,
                drinkStartTime,
                drinkEndTime,
                drinkStartTime != null ? drinkStartTime.toLocalDate() : null,
                null,
                now);
    }

    public static Calendar create(
            long ownerId,
            String title,
            float totalDrinkQuantity,
            LocalDate drinkDate
    ) {
        ZonedDateTime date = drinkDate.atStartOfDay(ZoneId.of("Asia/Seoul"));
        return new Calendar(ownerId, title, totalDrinkQuantity, date, date, drinkDate, null, ZonedDateTime.now());
    }

    public static Calendar createForMain(
            long ownerId,
            float totalDrinkQuantity,
            ZonedDateTime drinkStartTime,
            ZonedDateTime drinkEndTime
    ) {
        return new Calendar(
                ownerId,
                MAIN_CALENDAR_DEFAULT_TITLE,
                totalDrinkQuantity,
                drinkStartTime,
                drinkEndTime,
                drinkStartTime.toLocalDate(),
                null,
                ZonedDateTime.now());
    }

    public void addUserCalendars(Collection<UserCalendar> userCalendarEntries) {
        for (UserCalendar userCalendar : userCalendarEntries) {
            addUserCalendar(userCalendar);
        }
    }

    public void addUserCalendar(UserCalendar userCalendar) {
        userCalendars.add(userCalendar);
        userCalendar.setCalendar(this);
    }

    public void addPhotos(List<Photo> photos) {
        if (getCurrentPhotoCount() + CollectionUtils.size(photos) > MAX_PHOTO_COUNT) {
            throw new DomainException(CalendarError.IMAGE_LIMIT_EXCEEDED);
        }
        for (Photo photo : photos) {
            addPhoto(photo);
        }
    }

    public void deletePhoto(long userId, long photoId) {
        Photo findPhoto = photos.stream()
                .filter(photo -> photo.getId() == photoId)
                .findFirst()
                .orElseThrow(() -> new DomainException(PhotoError.NOT_FOUND_PHOTO));
        if (findPhoto.canBeDeletedBy(userId)) {
            findPhoto.delete();
        } else {
            throw new DomainException(PhotoError.NO_PERMISSION_TO_DELETE_PHOTO);
        }
    }

    public void addPhoto(Photo photo) {
        if (getCurrentPhotoCount() >= MAX_PHOTO_COUNT) {
            throw new DomainException(CalendarError.IMAGE_LIMIT_EXCEEDED);
        }
        photos.add(photo);
        photo.setCalendar(this);
    }

    /**
     * 현재 캘린더에서 삭제되지 않은 사진 갯수 조회
     *
     * @return
     */
    private int getCurrentPhotoCount() {
        return (int) photos.stream()
                .filter(photo -> !photo.isDeleted())
                .count();
    }

    // public void addComment(Comment comment) {
    //     comments.add(comment);
    //     comment.setCalendar(this);
    // }

    public void addDrinkRecords(long userId, Collection<DrinkRecord> drinkRecords) {
        UserCalendar userCalendar = getUserCalendarByUserId(userId);
        userCalendar.addDrinkRecords(drinkRecords);
    }

    public boolean isOwner(long userId) {
        return ownerId == userId;
    }

    public boolean hasPermission(long userId) {
        return userCalendars.stream()
                .anyMatch(userCalendar -> userCalendar.isOwner(userId));
    }

    public Optional<UserCalendar> findUserCalendarByUserId(long userId) {
        return userCalendars.stream()
                .filter(userCalendar -> userCalendar.isOwner(userId))
                .findFirst();
    }

    public List<UserCalendar> findUserCalendarsExcludingUserId(long userId) {
        return userCalendars.stream()
                .filter(userCalendar -> !userCalendar.isOwner(userId))
                .toList();
    }

    public void updateTitle(String newTitle) {
        validateTitle(newTitle);
        title = newTitle;
    }

    public void updateDrinkDate(LocalDate newDrinkDate) {
        drinkDate = newDrinkDate;
        drinkStartTime = newDrinkDate.atStartOfDay(ZoneId.of("Asia/Seoul"));
        drinkEndTime = newDrinkDate.atStartOfDay(ZoneId.of("Asia/Seoul"));
    }

    /**
     * 캘린더 데이터 업데이트
     *
     * @param newTitle
     * @param newDrinkStartTime
     * @param newDrinkEndTime
     */
    public void update(
            String newTitle,
            ZonedDateTime newDrinkStartTime,
            ZonedDateTime newDrinkEndTime,
            ZonedDateTime now
    ) {
        validateTitle(newTitle);
        validateDrinkStartTimeAndDrinkEndTime(newDrinkStartTime, newDrinkEndTime, now);

        title = newTitle;
        drinkStartTime = newDrinkStartTime;
        drinkEndTime = newDrinkEndTime;
    }

    private void validateTitle(String title) {
        if (title == null) {
            throw new DomainException(CalendarError.NULL_TITLE);
        }
        if (StringUtils.length(title) > 100) {
            throw new DomainException(CalendarError.TITLE_LENGTH_EXCEEDED);
        }
    }

    private void validateDrinkStartTimeAndDrinkEndTime(
            ZonedDateTime drinkStartTime,
            ZonedDateTime drinkEndTime,
            ZonedDateTime now
    ) {
        if (drinkStartTime == null) {
            throw new DomainException(CalendarError.NULL_DRINK_START_TIME);
        }
        if (drinkEndTime == null) {
            throw new DomainException(CalendarError.NULL_DRINK_END_TIME);
        }
        if (drinkStartTime.isAfter(drinkEndTime)) {
            throw new DomainException(CalendarError.START_TIME_AFTER_END_TIME);
        }
        if (drinkEndTime.isAfter(now)) {
            throw new DomainException(CalendarError.END_TIME_IN_FUTURE);
        }
    }

    public void updateContent(final long userCalendarId, final String newContent) {
        UserCalendar userCalendar = getUserCalendarById(userCalendarId);
        userCalendar.updateContent(newContent);
    }

    public void updateCondition(final long userCalendarId, final String newCondition) {
        UserCalendar userCalendar = getUserCalendarById(userCalendarId);
        userCalendar.updateCondition(newCondition);
    }

    private Optional<UserCalendar> findUserCalendarById(final long userCalendarId) {
        return userCalendars.stream()
                .filter(userCalendar -> userCalendar.getId().equals(userCalendarId))
                .findFirst();
    }

    private UserCalendar getUserCalendarById(final long userCalendarId) {
        return userCalendars.stream()
                .filter(userCalendar -> userCalendar.getId().equals(userCalendarId))
                .findFirst()
                .orElseThrow(() -> new DomainException(UserCalendarError.USER_CALENDAR_NOT_FOUND));
    }

    public void updateDrinkRecords(long userId, List<DrinkRecordUpdateVo> updateVo) {
        UserCalendar userCalendar = getUserCalendarByUserId(userId);
        userCalendar.updateDrinkRecord(userId, updateVo);
    }

    public void deleteDrinkRecords(long userId, List<Long> drinkRecordIdsToDelete) {
        UserCalendar userCalendar = getUserCalendarByUserId(userId);
        userCalendar.deleteDrinkRecords(userId, drinkRecordIdsToDelete);
    }

    private UserCalendar getUserCalendarByUserId(long userId) {
        return userCalendars.stream()
                .filter(userCalendar -> userCalendar.isOwner(userId))
                .findFirst()
                .orElseThrow(() -> new DomainException(UserCalendarError.USER_CALENDAR_NOT_FOUND));
    }

    public LocalDate getDrinkStartTimeLocalDate() {
        return getDrinkStartTimeLocalDate(ZoneId.of("Asia/Seoul"));
    }

    public LocalDate getDrinkStartTimeLocalDate(ZoneId zoneId) {
        return drinkStartTime.withZoneSameInstant(zoneId).toLocalDate();
    }

    public float calculateTotalDrinkQuantity() {
        return (float) userCalendars.stream()
                .flatMap(userCalendar -> userCalendar.getDrinkRecords().stream())
                .mapToDouble(DrinkRecord::getQuantity)
                .sum();
    }

    public DrinkType getMostConsumedDrinkType() {
        return userCalendars.stream()
                .flatMap(userCalendar -> userCalendar.getDrinkRecords().stream().filter(drinkRecord -> !drinkRecord.isDeleted()))
                .collect(Collectors.groupingBy(
                        DrinkRecord::getType,
                        Collectors.summingDouble(DrinkRecord::getQuantity)
                )).entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);
    }

    public int getTotalPrice() {
        return userCalendars.stream()
                .mapToInt(UserCalendar::totalPrice)
                .sum();
    }

    public int getTotalCalories() {
        return userCalendars.stream()
                .mapToInt(UserCalendar::totalCalories)
                .sum();
    }

    public void deleteUserCalendars(long userId, Collection<Long> userCalendarIds) {
        userCalendarIds.forEach(userCalendarId -> deleteUserCalendar(userId, userCalendarId));
    }

    public void deleteUserCalendar(long userId, long userCalendarId) {
        UserCalendar foundUserCalendar = userCalendars.stream()
                .filter(userCalendar -> userCalendar.getId() == userCalendarId)
                .findFirst()
                .orElseThrow(() -> new DomainException(UserCalendarError.USER_CALENDAR_NOT_FOUND));
        long foundUserId = foundUserCalendar.getUserId();
        if (!isOwner(userId) && foundUserId != userId) {
            throw new DomainException(UserCalendarError.NO_PERMISSION);
        }

        foundUserCalendar.delete();

        if (isOwner(foundUserId)) {
            userCalendars.stream()
                    .filter(userCalendar -> !userCalendar.isOwner(foundUserId))
                    .findFirst()
                    .ifPresentOrElse(
                            userCalendar -> ownerId = userCalendar.getUserId(),
                            () -> deletedAt = LocalDateTime.now()
                    );
        }

        photos.stream()
                .filter(photo -> photo.isOwner(foundUserId))
                .forEach(Photo::delete);
    }

    public List<UserCalendar> getTaggedUserCalendars() {
        return userCalendars.stream()
                .filter(userCalendar -> !isOwner(userCalendar.getUserId()))
                .toList();
    }

    public List<Photo> getPhotos() {
        return photos.stream()
                .filter(photo -> !photo.isDeleted())
                .toList();
    }
}
