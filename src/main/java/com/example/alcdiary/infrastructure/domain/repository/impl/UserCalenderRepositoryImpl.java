package com.example.alcdiary.infrastructure.domain.repository.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.example.alcdiary.infrastructure.entity.QCalender.calender;

@Repository
@RequiredArgsConstructor
public abstract class UserCalenderRepositoryImpl {
    private final JPAQueryFactory jpaQueryFactory;

    void findCalenders(String userId, Long calenderId) {
        jpaQueryFactory.select(calender).fetchAll();
    }
}
