package com.alc.diary.infrastructure.persistence;

import com.alc.diary.domain.user.repository.CustomUserRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class UserRepositoryImpl implements CustomUserRepository {
    private final JPAQueryFactory jpaQueryFactory;
}
