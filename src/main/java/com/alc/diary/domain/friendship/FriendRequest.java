package com.alc.diary.domain.friendship;

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
@Table(name = "friend_requests")
@Entity
public class FriendRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "sender_id", nullable = false, updatable = false)
    private long senderId;

    @Column(name = "receiver_id", nullable = false, updatable = false)
    private long receiverId;

    @Column(name = "message", length = 100, updatable = false)
    private String message;

    @Audited
    @Enumerated(EnumType.STRING)
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

    public void accept(long receiverId) {
        if (this.receiverId != receiverId) {
            throw new DomainException();
        }
        status = FriendRequestStatus.ACCEPTED;
    }
}
