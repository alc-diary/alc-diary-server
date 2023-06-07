package com.alc.diary.domain.user.repository;

import com.alc.diary.domain.user.User;
import com.alc.diary.domain.user.enums.SocialType;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.util.Optional;

public interface UserRepository extends Repository<User, Long>, CustomUserRepository {

    Optional<User> findById(Long id);

    boolean existsById(long id);

    @Query(value = "select case when count(u.id) = 1 then true else false end " +
                   "from users u " +
                   "where u.id = :id " +
                   "and u.deleted_at is null " +
                   "and u.status = 'ONBOARDING'",
            nativeQuery = true
    )
    boolean existsByIdAndStatusEqualsOnboarding(long id);

    @Query("select u " +
           "from User u " +
           "where u.id = :id")
    Optional<User> findByIdIgnoringWhere(Long id);

    User save(User user);

    Optional<User> findBySocialTypeAndSocialId(SocialType socialType, String socialId);

    @Query("select u " +
            "from User u " +
            "join fetch u.detail ud " +
            "where ud.nickname = :nickname")
    Optional<User> findByDetail_Nickname(String nickname);
}
