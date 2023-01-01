package com.example.alcdiary.infrastructure.jpa;

import com.example.alcdiary.infrastructure.entity.Calender;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CalenderRepository extends JpaRepository<Calender, Long> {

    Optional<Calender> findByUserIdAndId(String userId, Long id);

    @Query("delete from Calender where userId=:userId and id=:calenderId")
    void deleteCalenderById(Long calenderId, String userId);

}
