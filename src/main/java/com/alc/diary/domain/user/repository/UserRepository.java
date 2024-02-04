package com.alc.diary.domain.user.repository;

import com.alc.diary.domain.user.User;
import com.alc.diary.domain.user.enums.SocialType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface UserRepository extends Repository<User, Long>, CustomUserRepository {

    Page<User> findAll(Pageable pageable);

    Optional<User> findById(long id);

    User save(User user);

    @Query("SELECT u " +
            "FROM User u " +
            "JOIN FETCH u.detail ud " +
            "WHERE u.id = :id " +
            "AND u.status = com.alc.diary.domain.user.enums.UserStatus.ACTIVE ")
    Optional<User> findActiveUserById(long id);

    @Query("SELECT u " +
            "FROM User u " +
            "JOIN FETCH u.detail ud " +
            "WHERE u.id IN (:userIds) " +
            "AND u.status = com.alc.diary.domain.user.enums.UserStatus.ACTIVE ")
    List<User> findActiveUsersByIdIn(Collection<Long> userIds);

    @Query("SELECT u.id " +
            "FROM User u " +
            "WHERE u.id IN (:userIds) " +
            "AND u.status = com.alc.diary.domain.user.enums.UserStatus.ACTIVE ")
    Set<Long> findActiveUserIdsByIdIn(Collection<Long> userIds);

    @Query("SELECT u " +
            "FROM User u " +
            "WHERE u.id = :id " +
            "AND u.status = com.alc.diary.domain.user.enums.UserStatus.ONBOARDING")
    Optional<User> findOnboardingUserById(long id);

    @Query("SELECT u " +
            "FROM User u " +
            "WHERE u.id = :id " +
            "AND u.status <> com.alc.diary.domain.user.enums.UserStatus.DEACTIVATED")
    Optional<User> findNotDeactivatedUserById(long id);

    @Query("SELECT u " +
            "FROM User u " +
            "WHERE u.socialType = :socialType " +
            "AND u.socialId = :socialId " +
            "AND u.status <> com.alc.diary.domain.user.enums.UserStatus.DEACTIVATED")
    Optional<User> findNotDeactivatedUserByIdAndSocialTypeAndSocialId(SocialType socialType, String socialId);

    @Query("SELECT u " +
            "FROM User u " +
            "WHERE u.detail.nickname = :nickname " +
            "AND u.status = com.alc.diary.domain.user.enums.UserStatus.ACTIVE")
    Optional<User> findActiveUserByNickname(String nickname);
}
