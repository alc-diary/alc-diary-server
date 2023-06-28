package com.alc.diary.domain.friendship.repository;

import com.alc.diary.domain.friendship.FriendRequest;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Optional;

public interface FriendRequestRepository extends Repository<FriendRequest, Long> {

    FriendRequest save(FriendRequest friendRequest);

    Optional<FriendRequest> findById(long friendRequestId);

    @Query("SELECT fr " +
            "FROM FriendRequest fr " +
            "WHERE (" +
            "   (fr.senderId = :userAId AND fr.receiverId = :userBId) " +
            "       OR " +
            "   (fr.senderId = :userBId AND fr.receiverId = :userAId) " +
            ") " +
            "AND fr.status = com.alc.diary.domain.friendship.enums.FriendRequestStatus.PENDING ")
    Optional<FriendRequest> findPendingRequestWithUsers(long userAId, long userBId);

    @Query("SELECT fr " +
            "FROM FriendRequest fr " +
            "JOIN User u ON fr.senderId = u.id " +
            "WHERE fr.receiverId = :receiverId " +
            "AND u.status = com.alc.diary.domain.user.enums.UserStatus.ACTIVE ")
    List<FriendRequest> findPendingRequestsByReceiverId(long receiverId);

    @Query("SELECT fr " +
            "FROM FriendRequest fr " +
            "JOIN User u ON fr.receiverId = u.id " +
            "WHERE fr.senderId = :senderId " +
            "AND u.status = com.alc.diary.domain.user.enums.UserStatus.ACTIVE ")
    List<FriendRequest> findPendingRequestsBySenderId(long senderId);

    @Query("SELECT fr " +
           "FROM FriendRequest fr " +
           "WHERE ( " +
           "    (fr.senderId = :userAId AND fr.receiverId = :userBId) " +
           "        OR " +
           "    (fr.senderId = :userBId AND fr.receiverId =: uesrAId) " +
           "AND fr.status = com.alc.diary.domain.friendship.enums.FriendRequestStatus.ACCEPTED " +
           ")")
    Optional<FriendRequest> findAcceptedRequestWithUsers(long userAId, long userBId);
}
