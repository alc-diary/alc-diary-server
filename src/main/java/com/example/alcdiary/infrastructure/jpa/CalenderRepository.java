package com.example.alcdiary.infrastructure.jpa;

import com.example.alcdiary.infrastructure.entity.Calender;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CalenderRepository extends JpaRepository<Calender, Long> {
}
