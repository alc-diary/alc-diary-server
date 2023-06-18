package com.alc.diary.domain.friendship.repository;

import com.alc.diary.domain.friendship.Friendship;
import com.alc.diary.domain.friendship.enums.FriendshipStatus;
import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Optional;

public interface FriendshipRepository extends Repository<Friendship, Long> {

    Optional<Friendship> findById(long id);

    Optional<Friendship> findByIdAndStatusEquals(long id, FriendshipStatus status);

    List<Friendship> findByIdIn(List<Long> ids);

    Friendship save(Friendship friendship);

    void delete(Friendship friendship);

    void deleteById(long id);

    List<Friendship> findByToUser_Id(long toUserId);

    List<Friendship> findByToUser_IdAndStatusEquals(long toUserId, FriendshipStatus status);

    boolean existsByFromUser_IdAndToUser_Id(long fromUserId, long toUserId);

    List<Friendship> findByFromUser_IdAndToUser_Id(long fromUserId, long toUserId);
}
