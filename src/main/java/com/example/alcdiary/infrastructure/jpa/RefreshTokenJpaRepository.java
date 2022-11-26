package com.example.alcdiary.infrastructure.jpa;

import com.example.alcdiary.infrastructure.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenJpaRepository extends JpaRepository<RefreshToken, String> {
}
