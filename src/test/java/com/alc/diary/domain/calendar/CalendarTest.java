package com.alc.diary.domain.calendar;

import com.alc.diary.domain.calendar.error.CalendarError;
import com.alc.diary.domain.exception.DomainException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CalendarTest {

    private static final Clock now = Clock.fixed(Instant.parse("2023-07-02T12:00:00.000+09:00"), ZoneId.of("Asia/Seoul"));

    @DisplayName("캘린더 생성시 제목이 null이면 NULL_TITLE 예외가 발생한다.")
    @Test
    void givenNullTitle_whenCreatingCalendar_thenThrowsNULL_TITLE_Exception() {
        // given
        String title = null;

        // when & then
        assertThatThrownBy(() ->
                        Calendar.create(
                                1L,
                                title,
                                1.0f,
                                ZonedDateTime.of(2023, 7, 1, 18, 0, 0, 0, ZoneId.of("Asia/Seoul")),
                                ZonedDateTime.of(2023, 7, 1, 22, 0, 0, 0, ZoneId.of("Asia/Seoul")),
                                ZonedDateTime.now(now))
                )
                .isInstanceOf(DomainException.class)
                .hasMessage("Title cannot be null.")
                .extracting(ex -> ((DomainException) ex).getErrorModel())
                .isEqualTo(CalendarError.NULL_TITLE);
    }

    @DisplayName("캘린더 생성시 제목이 100자를 초과하면 TITLE_LENGTH_EXCEEDED 예외가 발생한다.")
    @ParameterizedTest
    @ValueSource(ints = {101, 102, 103})
    void givenTitleExceeds100_whenCreatingCalendar_thenThrowsTITLE_LENGTH_EXCEEDED_Exception(int input) {
        // given
        String title = "a".repeat(input);

        // when & then
        assertThatThrownBy(() ->
                Calendar.create(
                        1L,
                        title,
                        1.0f,
                        ZonedDateTime.of(2023, 7, 1, 18, 0, 0, 0, ZoneId.of("Asia/Seoul")),
                        ZonedDateTime.of(2023, 7, 1, 22, 0, 0, 0, ZoneId.of("Asia/Seoul")),
                        ZonedDateTime.now(now)))
                .isInstanceOf(DomainException.class)
                .hasMessage("Calendar title length cannot exceed 100 characters.")
                .extracting(ex -> ((DomainException) ex).getErrorModel())
                .isEqualTo(CalendarError.TITLE_LENGTH_EXCEEDED);
    }

    @DisplayName("캘린더 생성시 음주 시작 시간이 null이면 NULL_DRINK_START_TIME 예외가 발생한다.")
    @Test
    void givenNullDrinkStartTime_whenCreatingCalendar_thenThrowsNULL_DRINK_START_TIME_Exception() {
        // given
        ZonedDateTime drinkStartTime = null;

        // when & then
        assertThatThrownBy(() ->
                Calendar.create(
                        1L,
                        "test title",
                        1.0f,
                        drinkStartTime,
                        ZonedDateTime.of(2023, 7, 1, 22, 0, 0, 0, ZoneId.of("Asia/Seoul")),
                        ZonedDateTime.now(now)))
                .isInstanceOf(DomainException.class)
                .hasMessage("Drink start time cannot be null.")
                .extracting(ex -> ((DomainException) ex).getErrorModel())
                .isEqualTo(CalendarError.NULL_DRINK_START_TIME);
    }

    @DisplayName("캘린더 생성시 음주 종료 시간이 null이면 NULL_DRINK_END_TIME 예외가 발생한다.")
    @Test
    void givenNullDrinkEndTime_whenCreatingCalendar_thenThrowsNULL_DRINK_END_TIME_Exception() {
        // given
        ZonedDateTime drinkEndTime = null;

        // when & then
        assertThatThrownBy(() ->
                Calendar.create(
                        1L,
                        "test title",
                        1.0f,
                        ZonedDateTime.of(2023, 7, 1, 18, 0, 0, 0, ZoneId.of("Asia/Seoul")),
                        drinkEndTime,
                        ZonedDateTime.now(now)))
                .isInstanceOf(DomainException.class)
                .hasMessage("Drink end time cannot be null.")
                .extracting(ex -> ((DomainException) ex).getErrorModel())
                .isEqualTo(CalendarError.NULL_DRINK_END_TIME);
    }

    @DisplayName("캘린더 생성시 음주 시작 시간이 음주 종료 시간보다 뒤일 경우 START_TIME_AFTER_END_TIME 예외가 발생한다.")
    @Test
    void givenDrinkStartTimeAfterEndTime_whenCreatingCalendar_thenThrowsSTART_TIME_AFTER_END_TIME_Exception() {
        // given
        ZonedDateTime drinkStartTime = ZonedDateTime.of(2023, 7, 1, 18, 0, 0, 0, ZoneId.of("Asia/Seoul"));
        ZonedDateTime drinkEndTime = ZonedDateTime.of(2023, 7, 1, 16, 0, 0, 0, ZoneId.of("Asia/Seoul"));

        // when & then
        assertThatThrownBy(() ->
                Calendar.create(
                        1L,
                        "test title",
                        1.0f,
                        drinkStartTime,
                        drinkEndTime,
                        ZonedDateTime.now(now)))
                .isInstanceOf(DomainException.class)
                .hasMessage("Start time cannot be after end time.")
                .extracting(ex -> ((DomainException) ex).getErrorModel())
                .isEqualTo(CalendarError.START_TIME_AFTER_END_TIME);
    }

    @DisplayName("캘린더 생성 시 음주 종료시간이 현재 시간보다 뒤일 경우 END_TIME_IN_FUTURE 예외가 발생한다.")
    @Test
    void givenDrinkEndTimeInFutureWhenCreatingCalendar_thenThrowsEND_TIME_IN_FUTURE_Exception() {
        // given
        ZonedDateTime drinkStartTime = ZonedDateTime.of(2023, 7, 1, 18, 0, 0, 0, ZoneId.of("Asia/Seoul"));
        ZonedDateTime drinkEndTime = ZonedDateTime.of(2023, 7, 2, 16, 0, 0, 0, ZoneId.of("Asia/Seoul"));

        // when & then
        assertThatThrownBy(() ->
                Calendar.create(
                        1L,
                        "test title",
                        1.0f,
                        drinkStartTime,
                        drinkEndTime,
                        ZonedDateTime.now(now)))
                .isInstanceOf(DomainException.class)
                .hasMessage("End time cannot be int the future.")
                .extracting(ex -> ((DomainException) ex).getErrorModel())
                .isEqualTo(CalendarError.END_TIME_IN_FUTURE);
    }

    @DisplayName("유효한 제목이 주어졌을 때, 캘린더의 제목이 정상적으로 업데이트 된다.")
    @Test
    void givenValidTitle_whenUpdatingCalendar_thenTitleIsUpdated() {
        // given
        String newTitle = "New Title";
        Calendar calendar = Calendar.create(
                1L,
                "test title",
                1.5f,
                ZonedDateTime.of(2023, 7, 1, 18, 0, 0, 0, ZoneId.of("Asia/Seoul")),
                ZonedDateTime.of(2023, 7, 1, 18, 0, 0, 0, ZoneId.of("Asia/Seoul")),
                ZonedDateTime.now(now)
        );

        // when
        calendar.updateTitle(newTitle);

        // then
        assertThat(calendar.getTitle()).isEqualTo(newTitle);
    }

    @DisplayName("캘린더 업데이트시 title이 null이면 NULL_TITLE 예외가 발생한다.")
    @Test
    void givenNullTitle_whenUpdatingCalendar_thenThrowsNULL_TITLE_Exception() {
        // given
        String newTitle = null;
        Calendar calendar = Calendar.create(
                1L,
                "test title",
                1.5f,
                ZonedDateTime.of(2023, 7, 1, 18, 0, 0, 0, ZoneId.of("Asia/Seoul")),
                ZonedDateTime.of(2023, 7, 1, 18, 0, 0, 0, ZoneId.of("Asia/Seoul")),
                ZonedDateTime.now(now)
        );

        // when & then
        assertThatThrownBy(() -> calendar.updateTitle(newTitle))
                .isInstanceOf(DomainException.class)
                .hasMessage("Title cannot be null.")
                .extracting(ex -> ((DomainException) ex).getErrorModel())
                .isEqualTo(CalendarError.NULL_TITLE);
    }

    @DisplayName("캘린더 업데이트시 제목이 100자를 초과하면 TITLE_LENGTH_EXCEEDE 예외가 발생한다.")
    @ParameterizedTest
    @ValueSource(ints = {101, 102, 103})
    void givenTitleExceed100_whenUpdatingCalendar_thenThrowsTITLE_LENGTH_EXCEEDED_Exception(int input) {
        // given
        String newTitle = "a".repeat(input);

        Calendar calendar = Calendar.create(
                1L,
                "test title",
                1.5f,
                ZonedDateTime.of(2023, 7, 1, 18, 0, 0, 0, ZoneId.of("Asia/Seoul")),
                ZonedDateTime.of(2023, 7, 1, 18, 0, 0, 0, ZoneId.of("Asia/Seoul")),
                ZonedDateTime.now(now)
        );

        // when & then
        assertThatThrownBy(() -> calendar.updateTitle(newTitle))
                .isInstanceOf(DomainException.class)
                .hasMessage("Calendar title length cannot exceed 100 characters.")
                .extracting(ex -> ((DomainException) ex).getErrorModel())
                .isEqualTo(CalendarError.TITLE_LENGTH_EXCEEDED);
    }

    @DisplayName("캘린더와 사용자 ID가 있을 때, 사용자 ID가 캘린더 소유자 ID와 일치하면, isOwner()는 true를 반환한다.")
    @Test
    void givenCalendarAndUserId_whenUserIdMatchesOwnerId_thenIsOwnerReturnsTrue() {
        // given
        Calendar calendar = Calendar.create(
                1L,
                "test title",
                1.5f,
                ZonedDateTime.of(2023, 7, 1, 18, 0, 0, 0, ZoneId.of("Asia/Seoul")),
                ZonedDateTime.of(2023, 7, 1, 18, 0, 0, 0, ZoneId.of("Asia/Seoul")),
                ZonedDateTime.now(now)
        );
        long userId = 1L;

        // when & then
        assertThat(calendar.isOwner(userId)).isTrue();
    }

    @DisplayName("캘린더와 사용자 ID가 있을 때, 사용자 ID가 캘린더 소유자 ID와 일치하지 않으면, isOwner()는 false를 반환한다.")
    @Test
    void givenCalendarAndUserId_whenUserIdDoesNotMatchOwnerId_thenIsOwnerReturnsFalse() {
        // given
        Calendar calendar = Calendar.create(
                1L,
                "test title",
                1.5f,
                ZonedDateTime.of(2023, 7, 1, 18, 0, 0, 0, ZoneId.of("Asia/Seoul")),
                ZonedDateTime.of(2023, 7, 1, 18, 0, 0, 0, ZoneId.of("Asia/Seoul")),
                ZonedDateTime.now(now)
        );
        long userId = 2L;

        // when & then
        assertThat(calendar.isOwner(userId)).isFalse();
    }
}