package com.alc.diary.domain.user.repository;

import com.alc.diary.domain.user.UserGroup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserGroupRepository extends JpaRepository<UserGroup, Long> {

    Optional<UserGroup> findByName(String name);
}
