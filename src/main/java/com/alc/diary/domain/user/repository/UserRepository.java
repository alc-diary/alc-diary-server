package com.alc.diary.domain.user.repository;

import com.alc.diary.domain.user.User;
import com.alc.diary.domain.user.enums.SocialType;
import org.springframework.data.repository.Repository;

import java.util.Optional;

public interface UserRepository extends Repository<User, Long>, CustomUserRepository {

    Optional<User> findById(Long id);

    User save(User user);

    Optional<User> findBySocialTypeAndSocialId(SocialType socialType, String socialId);
}
