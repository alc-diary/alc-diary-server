package com.alc.diary.domain.user.repository;

public interface CustomUserRepository {

    boolean existsActiveUserById(long id);

    boolean existsOnboardingUserById(long id);

    boolean existsNotDeactivatedUserById(long id);
}
