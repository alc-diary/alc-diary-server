package com.alc.diary.domain.calendar;

import com.alc.diary.domain.calendar.error.CalendarError;
import com.alc.diary.domain.exception.DomainException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CalendarTest {

    private static final Clock now = Clock.fixed(Instant.parse("2023-07-02T12:00:00.000+09:00"), ZoneId.of("Asia/Seoul"));

    @Test
    void 캘린더생성시_title이_null이면_예외가발생한다() {
        assertThatThrownBy(() ->
                        Calendar.create(
                                1L,
                                null,
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

    @ParameterizedTest
    @ValueSource(ints = {101, 102, 103})
    void 캘린더생성시_title글자수가_100을_초과하면_예외가발생한다(int input) {
        String title = "a".repeat(input);
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

    @Test
    void 캘린더생성시_음주시작시간이_null일경우_예외가발생한다() {
        assertThatThrownBy(() ->
                Calendar.create(
                        1L,
                        "test title",
                        1.0f,
                        null,
                        ZonedDateTime.of(2023, 7, 1, 22, 0, 0, 0, ZoneId.of("Asia/Seoul")),
                        ZonedDateTime.now(now)))
                .isInstanceOf(DomainException.class)
                .hasMessage("Drink start time cannot be null.")
                .extracting(ex -> ((DomainException) ex).getErrorModel())
                .isEqualTo(CalendarError.NULL_DRINK_START_TIME);
    }

    @Test
    void 캘린더생성시_음주종료시간이_null일경우_예외가발생한다() {
        assertThatThrownBy(() ->
                Calendar.create(
                        1L,
                        "test title",
                        1.0f,
                        ZonedDateTime.of(2023, 7, 1, 18, 0, 0, 0, ZoneId.of("Asia/Seoul")),
                        null,
                        ZonedDateTime.now(now)))
                .isInstanceOf(DomainException.class)
                .hasMessage("Drink end time cannot be null.")
                .extracting(ex -> ((DomainException) ex).getErrorModel())
                .isEqualTo(CalendarError.NULL_DRINK_END_TIME);
    }

    @Test
    void 캘린더생성시_음주시작시간이_음주종료시간보다_뒤일경우_예외가발생한다() {
        ZonedDateTime drinkStartTime = ZonedDateTime.of(2023, 7, 1, 18, 0, 0, 0, ZoneId.of("Asia/Seoul"));
        ZonedDateTime drinkEndTime = ZonedDateTime.of(2023, 7, 1, 16, 0, 0, 0, ZoneId.of("Asia/Seoul"));
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

    @Test
    void 캘린더생성시_음주종료시간이_현재시간보다_뒤일경우_예외가발생한다() {
        ZonedDateTime drinkStartTime = ZonedDateTime.of(2023, 7, 1, 18, 0, 0, 0, ZoneId.of("Asia/Seoul"));
        ZonedDateTime drinkEndTime = ZonedDateTime.of(2023, 7, 2, 16, 0, 0, 0, ZoneId.of("Asia/Seoul"));
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

    @Test
    void isOwner() {
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

        // when
        // then
        assertThat(calendar.isOwner(userId)).isTrue();
    }

    @Test
    void updateTitle() {
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

    @Test
    void title을_null으로_업데이트하면_예외가발생한다() {
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

        // when
        // then
        assertThatThrownBy(() -> calendar.updateTitle(newTitle))
                .isInstanceOf(DomainException.class)
                .hasMessage("Title cannot be null.")
                .extracting(ex -> ((DomainException) ex).getErrorModel())
                .isEqualTo(CalendarError.NULL_TITLE);
    }

    @ParameterizedTest
    @ValueSource(ints = {101, 102, 103})
    void title을_100자이상으로_업데이트하면_예외가발생한다(int input) {
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

        // when
        // then
        assertThatThrownBy(() -> calendar.updateTitle(newTitle))
                .isInstanceOf(DomainException.class)
                .hasMessage("Calendar title length cannot exceed 100 characters.")
                .extracting(ex -> ((DomainException) ex).getErrorModel())
                .isEqualTo(CalendarError.TITLE_LENGTH_EXCEEDED);
    }

    /**
     * 권한 검증 로직이 도메인 바깥으로 빠져서 나중에 다시 테스트 예정
     *
     */
    // @ParameterizedTest
    // @ValueSource(ints = {2, 3, 4, 5})
    // void 캘린더의_owner가아닌유저가_title을업데이트하면_예외가발생한다(int input) {
    //     // given
    //     String newTitle = "New Title";
    //
    //     Calendar calendar = Calendar.create(
    //             1L,
    //             "test title",
    //             1.5f,
    //             ZonedDateTime.of(2023, 7, 1, 18, 0, 0, 0, ZoneId.of("Asia/Seoul")),
    //             ZonedDateTime.of(2023, 7, 1, 18, 0, 0, 0, ZoneId.of("Asia/Seoul")),
    //             ZonedDateTime.now(now)
    //     );
    //
    //     // when
    //     // then
    //     assertThatThrownBy(() -> calendar.updateTitle(newTitle))
    //             .isInstanceOf(DomainException.class)
    //             .hasMessage("You do not have permission to update title.")
    //             .extracting(ex -> ((DomainException) ex).getErrorModel())
    //             .isEqualTo(CalendarError.NO_PERMISSION_TO_UPDATE_TITLE);
    // }
}