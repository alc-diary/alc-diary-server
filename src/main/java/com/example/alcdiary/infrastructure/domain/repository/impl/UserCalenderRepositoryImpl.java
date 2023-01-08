package com.example.alcdiary.infrastructure.domain.repository.impl;

import com.example.alcdiary.domain.enums.EditRole;
import com.example.alcdiary.domain.model.calender.UserCalenderModel;
import com.example.alcdiary.infrastructure.entity.Calender;
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

    // 날짜 month, day

    // 상세조회
    public UserCalenderModel findCalenders(String userId, Long calenderId) {
        Calender result = jpaQueryFactory.select(calender)
                .from(calender)
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
}
