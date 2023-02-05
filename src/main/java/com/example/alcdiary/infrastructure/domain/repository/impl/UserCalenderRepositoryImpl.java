package com.example.alcdiary.infrastructure.domain.repository.impl;

import com.example.alcdiary.domain.enums.EditRole;
import com.example.alcdiary.domain.model.calender.CalenderAndUserDataModel;
import com.example.alcdiary.domain.model.calender.QCalenderAndUserDataModel;
import com.example.alcdiary.domain.model.calender.UserCalenderModel;
import com.example.alcdiary.infrastructure.entity.Calender;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

import static com.example.alcdiary.infrastructure.entity.QCalender.calender;
import static com.example.alcdiary.infrastructure.entity.QUser.user;
import static com.example.alcdiary.infrastructure.entity.QUserCalender.userCalender;

@Repository
@RequiredArgsConstructor
public class UserCalenderRepositoryImpl {
    private final JPAQueryFactory jpaQueryFactory;
    private BooleanBuilder builder = new BooleanBuilder();

    public List<CalenderAndUserDataModel> search(Integer month, Integer day, String userId) {
        List<CalenderAndUserDataModel> models = jpaQueryFactory.select(new QCalenderAndUserDataModel(calender.id, calender.title, calender.drinks,
                        userCalender.userId, calender.drinkStartTime, calender.drinkEndTime)).from(calender)
                .leftJoin(userCalender).on(userCalender.calenderId.eq(calender.id)).fetchJoin()
                .where(isDay(month, day).and(userCalender.userId.eq(userId))).fetchAll().stream().toList();

        models.forEach(model ->
                model.getUserProfileImageUrls().addAll(getUserProfiles(model.getCalenderId())));

        return models;
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


    public Collection<? extends String> getUserProfiles(Long calenderId) {
        return jpaQueryFactory.select(user.profileImageUrl).from(user)
                .leftJoin(userCalender).on(userCalender.userId.eq(user.id))
                .where(userCalender.editRole.eq(EditRole.VIEWER).and(userCalender.calenderId.eq(calenderId)))
                .fetch();
    }
}
