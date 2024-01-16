package com.alc.diary.domain.user.repository;

import java.util.List;

public interface CustomUserRepository {

    boolean existsActiveUserById(long id);

    boolean existsOnboardingUserById(long id);

    boolean existsNotDeactivatedUserById(long id);

    List<Long> findNotificationEnabledUserIdsWithToken();
}
