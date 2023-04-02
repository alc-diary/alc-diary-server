package com.alc.diary.domain.user.repository;

import com.alc.diary.domain.user.User;
import org.springframework.data.repository.Repository;

import java.util.Optional;

public interface UserRepository extends Repository<User, Long> {

    Optional<User> findById(Long id);
}
