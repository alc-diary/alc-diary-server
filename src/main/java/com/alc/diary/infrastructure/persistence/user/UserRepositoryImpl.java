package com.alc.diary.infrastructure.persistence.user;

import com.alc.diary.domain.user.enums.UserStatus;
import com.alc.diary.domain.user.repository.CustomUserRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.alc.diary.domain.user.QUser.user;

@RequiredArgsConstructor
@Repository
public class UserRepositoryImpl implements CustomUserRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public boolean existsActiveUserById(long id) {
        Integer fetchOne = jpaQueryFactory
                .selectOne()
                .from(user)
                .where(user.id.eq(id).and(user.status.eq(UserStatus.ACTIVE)))
                .fetchFirst();
        return fetchOne != null;
    }

    @Override
    public boolean existsOnboardingUserById(long id) {
        Integer fetchOne = jpaQueryFactory
                .selectOne()
                .from(user)
                .where(user.id.eq(id).and(user.status.eq(UserStatus.ONBOARDING)))
                .fetchFirst();
        return fetchOne != null;
    }

    @Override
    public boolean existsNotDeactivatedUserById(long id) {
        Integer fetchOne = jpaQueryFactory
                .selectOne()
                .from(user)
                .where(user.id.eq(id).and(user.status.ne(UserStatus.DEACTIVATED)))
                .fetchFirst();
        return fetchOne != null;
    }
}
