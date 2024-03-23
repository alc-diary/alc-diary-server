package com.alc.diary.domain.user.repository;

import com.alc.diary.domain.user.UserGroupMembership;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserGroupMembershipRepository extends JpaRepository<UserGroupMembership, Long> {
}
