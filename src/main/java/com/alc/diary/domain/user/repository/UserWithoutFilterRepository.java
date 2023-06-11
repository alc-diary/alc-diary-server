package com.alc.diary.domain.user.repository;

import com.alc.diary.domain.user.UserWithoutFilter;
import org.springframework.data.repository.Repository;

import java.util.Optional;

public interface UserWithoutFilterRepository extends Repository<UserWithoutFilter, Long> {

    Optional<UserWithoutFilter> findByIdAndDeletedAtIsNull(long userId);
}
