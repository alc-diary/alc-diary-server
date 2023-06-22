package com.alc.diary.domain.friendship.repository;

import com.alc.diary.domain.friendship.Friendship;
import com.alc.diary.domain.friendship.enums.FriendshipStatus;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Optional;

public interface FriendshipRepository extends Repository<Friendship, Long> {

    Optional<Friendship> findById(long id);

    List<Friendship> findByIdIn(List<Long> ids);

    Friendship save(Friendship friendship);

    void delete(Friendship friendship);

    List<Friendship> findByToUser_IdAndStatusEquals(long toUserId, FriendshipStatus status);

    List<Friendship> findByFromUser_IdAndToUser_Id(long fromUserId, long toUserId);

    @Query("SELECT f " +
           "FROM Friendship f " +
           "JOIN FETCH f.fromUser fu " +
           "JOIN FETCH f.toUser tu " +
           "WHERE " +
           "    (fu.id = :user1Id AND tu.id = :user2Id) " +
           "        OR " +
           "    (fu.id = :user2Id AND tu.id = :user1Id) " +
           "AND f.status = com.alc.diary.domain.friendship.enums.FriendshipStatus.ACCEPTED ")
    Optional<Friendship> findAcceptedFriendshipBetweenUsers(long user1Id, long user2Id);

    @Query("SELECT f " +
           "FROM Friendship f " +
           "JOIN FETCH f.fromUser fu " +
           "JOIN FETCH f.toUser tu " +
           "WHERE " +
           "    (fu.id = :user1Id AND tu.id = :user2Id) " +
           "        OR " +
           "    (fu.id = :user2Id AND tu.id = :user1Id) " +
           "AND f.status = com.alc.diary.domain.friendship.enums.FriendshipStatus.ACCEPTED ")
    Optional<Friendship> findRequestedFriendshipBetweenUsers(long user1Id, long user2Id);

    @Query("SELECT f " +
            "FROM Friendship f " +
            "JOIN FETCH f.fromUser fu " +
            "JOIN FETCH f.toUser tu " +
            "WHERE (" +
            "   f.fromUser.id = :userId " +
            "       OR" +
            "   f.toUser.id = :userId " +
            ")" +
            "AND f.status = 'ACCEPTED'")
    List<Friendship> findAcceptedFriendshipsByUserId(long userId);

    @Query("SELECT f " +
            "FROM Friendship f " +
            "WHERE f.fromUser.id = :fromUserId " +
            "AND f.status = 'REQUESTED'")
    List<Friendship> findRequestedFriendshipByFromUserId(long fromUserId);
}
