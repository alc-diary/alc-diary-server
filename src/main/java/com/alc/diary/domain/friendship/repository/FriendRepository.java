package com.alc.diary.domain.friendship.repository;

import com.alc.diary.domain.friendship.Friend;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Optional;

public interface FriendRepository extends Repository<Friend, Long> {

    Friend save(Friend friend);

    Optional<Friend> findById(long id);

    @Query("SELECT f " +
            "FROM Friend f " +
            "JOIN User ua ON f.userAId = ua.id " +
            "JOIN User ub ON f.userBId = ub.id " +
            "WHERE (f.userAId = :userId OR f.userBId = :userId) " +
            "AND ua.status = com.alc.diary.domain.user.enums.UserStatus.ACTIVE " +
            "AND ub.status = com.alc.diary.domain.user.enums.UserStatus.ACTIVE" +
            "")
    List<Friend> findByUserId(long userId);

    @Query("SELECT f " +
            "FROM Friend f " +
            "JOIN User ua ON f.userAId = ua.id " +
            "JOIN User ub ON f.userBId = ub.id " +
            "WHERE (" +
            "   (f.userAId = :user1Id AND f.userBId = :user2Id) " +
            "       OR " +
            "   (f.userAId = :user2Id AND f.userBId = :user1Id) " +
            ") " +
            "AND ua.status = com.alc.diary.domain.user.enums.UserStatus.ACTIVE " +
            "AND ub.status = com.alc.diary.domain.user.enums.UserStatus.ACTIVE ")
    Optional<Friend> findWithUsers(long user1Id, long user2Id);
}
