package com.alc.diary.domain.friendship;

import com.alc.diary.domain.BaseEntity;
import com.alc.diary.domain.exception.DomainException;
import com.alc.diary.domain.friendship.enums.FriendshipStatus;
import com.alc.diary.domain.friendship.error.FriendshipError;
import com.alc.diary.domain.user.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Getter
@ToString(exclude = {"fromUser", "toUser"})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "friendships")
@Entity
public class Friendship extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_user_id", updatable = false)
    private User fromUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_user_id", updatable = false)
    private User toUser;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private FriendshipStatus status;

    @Column(name = "message", length = 100, updatable = false)
    private String message;

    private Friendship(Long id, User fromUser, User toUser, FriendshipStatus status, String message) {
        if (fromUser == null) {
            throw new IllegalArgumentException();
        }
        if (toUser == null) {
            throw new IllegalArgumentException();
        }
        if (status == null) {
            throw new IllegalArgumentException();
        }
        if (message != null && message.length() > 100) {
            throw new IllegalArgumentException();
        }
        this.id = id;
        this.fromUser = fromUser;
        this.toUser = toUser;
        this.status = status;
        this.message = message;
    }

    public static Friendship request(User fromUser, User toUser, String message) {
        return new Friendship(null, fromUser, toUser, FriendshipStatus.REQUESTED, message);
    }

    public void accept(long userId) {
        if (userId != toUser.getId()) {
            throw new DomainException(FriendshipError.INVALID_REQUEST);
        }
        if (status != FriendshipStatus.REQUESTED) {
            throw new DomainException(FriendshipError.INVALID_REQUEST);
        }
        status = FriendshipStatus.ACCEPTED;
    }

    public void decline(long userId) {
        if (userId != toUser.getId()) {
            throw new DomainException(FriendshipError.INVALID_REQUEST);
        }
        if (status != FriendshipStatus.REQUESTED) {
            throw new DomainException(FriendshipError.INVALID_REQUEST);
        }
        status = FriendshipStatus.DECLINED;
    }

    public boolean isUserInvolvedInFriendship(long userId) {
        return userId == fromUser.getId() || userId == toUser.getId();
    }
}
