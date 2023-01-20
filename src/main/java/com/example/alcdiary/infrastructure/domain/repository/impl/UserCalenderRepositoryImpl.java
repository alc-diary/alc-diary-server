package com.example.alcdiary.infrastructure.domain.repository.impl;

import com.example.alcdiary.domain.enums.EditRole;
import com.example.alcdiary.domain.model.calender.UserCalenderModel;
import com.example.alcdiary.infrastructure.entity.Calender;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.example.alcdiary.infrastructure.entity.QCalender.calender;
import static com.example.alcdiary.infrastructure.entity.QUserCalender.userCalender;

@Repository
@RequiredArgsConstructor
public class UserCalenderRepositoryImpl {
    private final JPAQueryFactory jpaQueryFactory;

    public List<Calender> search(Integer month, Integer day, String userId) {
        // calender 말고 user 정보도 같이 넘겨야함.
        return jpaQueryFactory.select(calender)
                .from(userCalender)
                .leftJoin(calender).on(calender.id.eq(userCalender.calenderId)).fetchJoin()
                .where(userCalender.userId.eq(userId), isDay(month, day))
                .fetchAll()
                .stream().toList();
    }


    // 상세조회
    public UserCalenderModel findCalenders(String userId, Long calenderId) {
        Calender result = jpaQueryFactory.select(calender)
                .from(calender)
                .leftJoin(userCalender).on(userCalender.calenderId.eq(calenderId))
                .where(calender.id.eq(calenderId).and(calender.userId.eq(userId)))
                .fetchOne();
        return new UserCalenderModel(result, findUserCalenderIds(calenderId));
    }

    private List<String> findUserCalenderIds(Long calenderId) {
        JPQLQuery<String> userCalenders = jpaQueryFactory.select(userCalender.userId)
                .from(userCalender)
                .where(userCalender.calenderId.eq(calenderId)
                        .and(userCalender.editRole.eq(EditRole.VIEWER)))
                .fetchAll();
        return userCalenders.stream().toList();
    }

    private BooleanExpression isDay(Integer month, Integer day) {
        if (month == null && day == null) return null;
        if (day != null) {
            return calender.createdAt.dayOfMonth().eq(day);
        }
        return calender.createdAt.month().between(month, month + 1);
    }
}
