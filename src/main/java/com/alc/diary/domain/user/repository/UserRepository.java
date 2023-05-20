package com.alc.diary.domain.user.repository;

import com.alc.diary.domain.user.User;
import com.alc.diary.domain.user.enums.SocialType;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.util.Optional;

public interface UserRepository extends Repository<User, Long>, CustomUserRepository {

    Optional<User> findById(Long id);

    @Query("select u " +
           "from User u " +
           "where u.id = :id")
    Optional<User> findByIdIgnoringWhere(Long id);

    User save(User user);

    Optional<User> findBySocialTypeAndSocialId(SocialType socialType, String socialId);

    Optional<User> findByNickname(String nickname);

    Boolean existsByNickname(String nickname);
}
