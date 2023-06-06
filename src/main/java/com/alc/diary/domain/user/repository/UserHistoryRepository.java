package com.alc.diary.domain.user.repository;

import com.alc.diary.domain.user.UserHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserHistoryRepository extends JpaRepository<UserHistory, Long> {
}
