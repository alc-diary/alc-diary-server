package com.alc.diary.domain.user.repository;

import com.alc.diary.domain.user.OnboardingUser;
import org.springframework.data.repository.Repository;

import java.util.Optional;

public interface OnboardingUserRepository extends Repository<OnboardingUser, Long> {

    Optional<OnboardingUser> findById(long id);
}
