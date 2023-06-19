package com.alc.diary.domain.friendship.repository;

import com.alc.diary.domain.friendship.Friendship;
import com.alc.diary.domain.friendship.enums.FriendshipStatus;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Optional;

public interface FriendshipRepository extends Repository<Friendship, Long> {

    Optional<Friendship> findById(long id);

    Optional<Friendship> findByIdAndStatusEquals(long id, FriendshipStatus status);

    List<Friendship> findByIdIn(List<Long> ids);

    Friendship save(Friendship friendship);

    void delete(Friendship friendship);

    List<Friendship> findByToUser_IdAndStatusEquals(long toUserId, FriendshipStatus status);

    List<Friendship> findByFromUser_IdAndToUser_Id(long fromUserId, long toUserId);

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
}
