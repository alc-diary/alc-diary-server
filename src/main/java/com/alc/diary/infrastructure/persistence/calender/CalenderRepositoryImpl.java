package com.alc.diary.infrastructure.persistence.calender;

import com.alc.diary.domain.calender.Calender;
import com.alc.diary.domain.calender.enums.QueryType;
import com.alc.diary.domain.calender.repository.CustomCalenderRepository;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Repository;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static com.alc.diary.domain.calender.QCalender.calender;

@Repository
@RequiredArgsConstructor
public class CalenderRepositoryImpl implements CustomCalenderRepository {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Calender> search(long userId, QueryType queryType, LocalDate date) {
        return jpaQueryFactory.selectFrom(calender)
                .where(calender.user.id.eq(userId)
                        .and(perQuery(queryType, date)))
                .fetch();
    }

    @Override
    public long countAlcoholLimit(long userId) {
        LocalDateTime today = LocalDateTime.now();
        val year = today.getYear();

        val start = today.with(DayOfWeek.MONDAY).getDayOfMonth();
        val end = today.with(DayOfWeek.SUNDAY).getDayOfMonth();
        return jpaQueryFactory.selectFrom(calender)
                .where(calender.user.id.eq(userId)
                        .and(calender.drinkStartDateTime.dayOfMonth().goe(start))
                        .and(calender.drinkStartDateTime.dayOfMonth().loe(end))
                        .and(calender.drinkStartDateTime.month().eq(today.getMonth().getValue()))
                        .and(calender.drinkStartDateTime.year().eq(year)))
                .fetch().size();
    }

    private BooleanExpression perQuery(QueryType queryType, LocalDate date) {
        val year = date.getYear();
        val month = date.getMonth().getValue();
        val day = date.getDayOfMonth();

        return switch (queryType) {
            case MONTH ->
                    calender.drinkStartDateTime.month().between(month, month + 1).and(calender.drinkStartDateTime.year().eq(year));
            case DAY ->
                    calender.drinkStartDateTime.dayOfMonth().eq(day).and(calender.drinkStartDateTime.month().eq(month)).and(calender.drinkStartDateTime.year().eq(year));
        };
    }
}
