package com.example.alcdiary.infrastructure.jpa;

import com.example.alcdiary.infrastructure.entity.Calender;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CalenderRepository extends JpaRepository<Calender, Long> {

    Optional<Calender> findByUserIdAndId(String userId, Long id);

    void deleteCalenderByUserIdAndId(String userId,Long calenderId);
}
