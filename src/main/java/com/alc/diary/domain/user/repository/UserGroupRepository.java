package com.alc.diary.domain.user.repository;

import com.alc.diary.domain.user.UserGroup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserGroupRepository extends JpaRepository<UserGroup, Long> {
}
