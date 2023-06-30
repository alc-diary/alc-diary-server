package com.alc.diary.domain.user.repository;

import com.alc.diary.domain.user.User;
import com.alc.diary.domain.user.enums.SocialType;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends Repository<User, Long>, CustomUserRepository {

    User save(User user);

    @Query("SELECT u " +
            "FROM User u " +
            "JOIN FETCH u.detail ud " +
            "WHERE u.id = :id " +
            "AND u.status = 'ACTIVE'")
    Optional<User> findActiveUserById(long id);

    @Query("SELECT u " +
            "FROM User u " +
            "JOIN FETCH u.detail ud " +
            "WHERE u.id IN (:userIds) " +
            "AND u.status = 'ACTIVE'")
    List<User> findActiveUsersByIdIn(Iterable<Long> userIds);

    @Query("SELECT u " +
            "FROM User u " +
            "WHERE u.id = :id " +
            "AND u.status = 'ONBOARDING'")
    Optional<User> findOnboardingUserById(long id);

    @Query("SELECT u " +
            "FROM User u " +
            "WHERE u.id = :id " +
            "AND u.status <> 'DEACTIVATED'")
    Optional<User> findNotDeactivatedUserById(long id);

    @Query("SELECT u " +
            "FROM User u " +
            "WHERE u.socialType = :socialType " +
            "AND u.socialId = :socialId " +
            "AND u.status <> 'DEACTIVATED'")
    Optional<User> findNotDeactivatedUserByIdAndSocialTypeAndSocialId(SocialType socialType, String socialId);

    @Query("SELECT u " +
            "FROM User u " +
            "WHERE u.detail.nickname = :nickname " +
            "AND u.status = 'ACTIVE'")
    Optional<User> findActiveUserByNickname(String nickname);
}
