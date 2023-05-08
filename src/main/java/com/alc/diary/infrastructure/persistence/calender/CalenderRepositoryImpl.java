package com.alc.diary.infrastructure.persistence.calender;

import com.alc.diary.domain.calender.Calender;
import com.alc.diary.domain.calender.enums.QueryType;
import com.alc.diary.domain.calender.repository.CustomCalenderRepository;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
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

    private BooleanExpression perQuery(QueryType queryType, LocalDate date) {
        val month = date.getMonth().getValue();
        val day = date.getDayOfMonth();

        return switch (queryType) {
            case MONTH -> calender.createdAt.month().between(month, month + 1);
            case DAY -> calender.createdAt.dayOfMonth().eq(day);
        };
    }
}
