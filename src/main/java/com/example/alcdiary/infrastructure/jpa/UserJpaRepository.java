package com.example.alcdiary.infrastructure.jpa;

import com.example.alcdiary.domain.model.UserModel;
import com.example.alcdiary.infrastructure.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserJpaRepository extends JpaRepository<User, Long> {

    Optional<User> findByIdAndSocialType(Long id, UserModel.SocialType socialType);
}
