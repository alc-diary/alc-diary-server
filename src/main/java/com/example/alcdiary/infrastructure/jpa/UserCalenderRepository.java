package com.example.alcdiary.infrastructure.jpa;

import com.example.alcdiary.infrastructure.entity.UserCalender;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserCalenderRepository extends JpaRepository<UserCalender, String> {
    Optional<UserCalender> findUserCalenderByUserIdAndCalenderId(String userId, Long calenderId);
}
