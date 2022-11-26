package com.example.alcdiary.infrastructure.jpa;

import com.example.alcdiary.infrastructure.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserJpaRepository extends JpaRepository<User, String> {
}
