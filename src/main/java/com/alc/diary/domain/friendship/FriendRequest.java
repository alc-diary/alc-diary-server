package com.alc.diary.domain.friendship;

import com.alc.diary.domain.BaseEntity;
import com.alc.diary.domain.exception.DomainException;
import com.alc.diary.domain.friendship.enums.FriendRequestStatus;
import com.alc.diary.domain.friendship.error.FriendRequestError;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.envers.Audited;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
        name = "friend_requests",
        indexes = {
                @Index(name = "idx_friend_requests_sender_id_receiver_id", columnList = "sender_id,receiver_id"),
                @Index(name = "idx_friend_requests_receiver_id_sender_id", columnList = "receiver_id,sender_id")
        }
)
@Entity
public class FriendRequest extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Audited
    @Column(name = "sender_id", nullable = false, updatable = false)
    private long senderId;

    @Audited
    @Column(name = "receiver_id", nullable = false, updatable = false)
    private long receiverId;

    @Audited
    @Column(name = "message", length = 100, updatable = false)
    private String message;

    @Audited
    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 30, nullable = false)
    private FriendRequestStatus status;

    private FriendRequest(Long senderId, Long receiverId, String message, FriendRequestStatus status) {
        if (senderId == null) {
            throw new DomainException(FriendRequestError.SENDER_ID_NULL);
        }
        if (receiverId == null) {
            throw new DomainException(FriendRequestError.RECEIVER_ID_NULL);
        }
        if (StringUtils.length(message) > 100) {
            throw new DomainException(FriendRequestError.MESSAGE_LENGTH_EXCEEDED);
        }
        if (status == null) {
            throw new DomainException(FriendRequestError.STATUS_NULL);
        }
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.message = message;
        this.status = status;
    }

    public static FriendRequest create(Long senderId, Long receiverId, String message) {
        return new FriendRequest(senderId, receiverId, message, FriendRequestStatus.PENDING);
    }

    public void markAccepted(long receiverId) {
        if (this.receiverId != receiverId) {
            throw new DomainException(FriendRequestError.NO_PERMISSION);
        }
        if (status != FriendRequestStatus.PENDING) {
            throw new DomainException(FriendRequestError.INVALID_REQUEST);
        }
        status = FriendRequestStatus.ACCEPTED;
    }

    public void markRejected(long receiverId) {
        if (this.receiverId != receiverId) {
            throw new DomainException(FriendRequestError.NO_PERMISSION);
        }
        if (status != FriendRequestStatus.PENDING) {
            throw new DomainException(FriendRequestError.INVALID_REQUEST);
        }
        status = FriendRequestStatus.REJECTED;
    }

    public void markCanceled(long senderId) {
        if (this.senderId != senderId) {
            throw new DomainException(FriendRequestError.NO_PERMISSION);
        }
        if (status != FriendRequestStatus.PENDING) {
            throw new DomainException(FriendRequestError.INVALID_REQUEST);
        }
        status = FriendRequestStatus.CANCELED;
    }

    public void markFriendshipEnded(long userId) {
        if (senderId != userId && receiverId != userId) {
            throw new DomainException(FriendRequestError.NO_PERMISSION);
        }
        if (status != FriendRequestStatus.ACCEPTED) {
            throw new DomainException(FriendRequestError.INVALID_REQUEST);
        }
        status = FriendRequestStatus.FRIENDSHIP_ENDED;
    }
}
