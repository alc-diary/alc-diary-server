package com.alc.diary.infrastructure.persistence.calender;

import com.alc.diary.domain.calender.repository.CustomCalenderRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class CalenderRepositoryImpl implements CustomCalenderRepository {
    private final JPAQueryFactory jpaQueryFactory;
}
