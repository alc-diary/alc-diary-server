package com.alc.diary.domain.friendship.repository;

import com.alc.diary.domain.friendship.Friendship;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Optional;

public interface FriendshipRepository extends Repository<Friendship, Long> {

    Friendship save(Friendship friendship);

    Optional<Friendship> findById(long id);

    void deleteById(long id);

    @Query("SELECT f " +
            "FROM Friendship f " +
            "JOIN User ua ON f.userAId = ua.id " +
            "JOIN User ub ON f.userBId = ub.id " +
            "WHERE (f.userAId = :userId OR f.userBId = :userId) " +
            "AND ua.status = com.alc.diary.domain.user.enums.UserStatus.ACTIVE " +
            "AND ub.status = com.alc.diary.domain.user.enums.UserStatus.ACTIVE" +
            "")
    List<Friendship> findByUserId(long userId);

    @Query("SELECT f " +
            "FROM Friendship f " +
            "JOIN User ua ON f.userAId = ua.id " +
            "JOIN User ub ON f.userBId = ub.id " +
            "WHERE (" +
            "   (f.userAId = :user1Id AND f.userBId = :user2Id) " +
            "       OR " +
            "   (f.userAId = :user2Id AND f.userBId = :user1Id) " +
            ") " +
            "AND ua.status = com.alc.diary.domain.user.enums.UserStatus.ACTIVE " +
            "AND ub.status = com.alc.diary.domain.user.enums.UserStatus.ACTIVE ")
    Optional<Friendship> findWithUsers(long user1Id, long user2Id);
}
