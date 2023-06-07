package com.alc.diary.infrastructure.persistence.user;

import com.alc.diary.domain.user.QUser;
import com.alc.diary.domain.user.repository.CustomUserRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.alc.diary.domain.user.QUser.user;

@RequiredArgsConstructor
@Repository
public class UserRepositoryImpl implements CustomUserRepository {

    private final JPAQueryFactory jpaQueryFactory;
}
