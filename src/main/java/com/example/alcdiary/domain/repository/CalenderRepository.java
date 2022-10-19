package com.example.alcdiary.domain.repository;

import com.example.alcdiary.infrastructure.entity.Calender;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CalenderRepository extends JpaRepository<Calender, Long> {
}
