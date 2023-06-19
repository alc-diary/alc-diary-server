package com.alc.diary.domain.user.repository;

import com.alc.diary.domain.user.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends Repository<User, Long>, CustomUserRepository {

    Optional<User> findById(Long id);

    boolean existsById(long id);

    @Query(value = "SELECT CASE " +
                   "    WHEN EXISTS(SELECT u.id FROM users u WHERE u.id = :id AND u.status <> 'DEACTIVATED') " +
                   "        THEN TRUE " +
                   "        ELSE FALSE " +
                   "    END ",
            nativeQuery = true
    )
    boolean existsByIdAndStatusNotEqualDeactivated(long id);

    @Query(
            value = "" +
                    "SELECT *                       \n" +
                    "FROM users u                   \n" +
                    "WHERE u.id = :id               \n" +
                    "AND u.status <> 'DEACTIVATED'  \n",
            nativeQuery = true
    )
    Optional<User> findByIdAndStatusNotEqualDeactivated(long id);

    @Query(value = "select * " +
                   "from users u " +
                   "where u.id = :id " +
                   "and u.status = 'ONBOARDING'",
            nativeQuery = true
    )
    Optional<User> findByIdAndStatusEqualsOnboarding(long id);

    User save(User user);

    @Query(
            value = "select * " +
                    "from users u " +
                    "where u.social_id = :socialId " +
                    "and u.social_type = :socialType " +
                    "and u.status <> 'DEACTIVATED' ",
            nativeQuery = true
    )
    Optional<User> findBySocialTypeAndSocialIdAndStatusNotEqualDeactivated(String socialType, String socialId);

    @Query("select u " +
            "from User u " +
            "join fetch u.detail ud " +
            "where ud.nickname = :nickname")
    Optional<User> findByDetail_Nickname(String nickname);

    List<User> findByIdIn(Iterable<Long> userIds);
}
