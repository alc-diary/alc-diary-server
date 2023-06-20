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
import org.apache.commons.lang3.StringUtils;

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

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "from_user_id", foreignKey = @ForeignKey(name = "fk_friendships_from_user_users"), updatable = false)
    private User fromUser;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "to_user_id", foreignKey = @ForeignKey(name = "fk_friendships_to_user_users"),updatable = false)
    private User toUser;

    @Column(name = "from_user_alias", length = 30)
    private String fromUserAlias;

    @Column(name = "to_user_alias", length = 30)
    private String toUserAlias;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private FriendshipStatus status;

    @Column(name = "message", length = 100, updatable = false)
    private String message;

    private Friendship(
            User fromUser,
            User toUser,
            String fromUserAlias,
            String toUserAlias,
            FriendshipStatus status,
            String message
    ) {
        if (fromUser == null) {
            throw new DomainException(FriendshipError.FROM_USER_NULL);
        }
        if (toUser == null) {
            throw new DomainException(FriendshipError.TO_USER_NULL);
        }
        if (StringUtils.length(message) > 100) {
            throw new DomainException(FriendshipError.MESSAGE_LENGTH_EXCEEDED);
        }
        if (StringUtils.length(fromUserAlias) > 30) {
            throw new DomainException(FriendshipError.USER_ALIAS_LENGTH_EXCEEDED);
        }
        if (StringUtils.length(toUserAlias) > 30) {
            throw new DomainException(FriendshipError.USER_ALIAS_LENGTH_EXCEEDED);
        }

        this.fromUser = fromUser;
        this.toUser = toUser;
        this.fromUserAlias = fromUserAlias;
        this.toUserAlias = toUserAlias;
        this.status = status != null ? status : FriendshipStatus.REQUESTED;
        this.message = message;
    }

    public static Friendship createRequest(
            User fromUser,
            User toUser,
            String fromUserAlias,
            String message
    ) {
        return new Friendship(
                fromUser,
                toUser,
                fromUserAlias,
                null,
                FriendshipStatus.REQUESTED,
                message
        );
    }

    public void accept(long userId) {
        if (userId != toUser.getId()) {
            throw new DomainException(FriendshipError.NO_PERMISSION_TO_ACCEPT);
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

    public void delete(long requestUserId) {
        if (requestUserId != fromUser.getId() && requestUserId != toUser.getId()) {
            throw new DomainException(FriendshipError.NO_PERMISSION_TO_DELETE);
        }
        status = FriendshipStatus.DELETED;
    }

    public boolean isUserInvolvedInFriendship(long userId) {
        return userId == fromUser.getId() || userId == toUser.getId();
    }

    public String getOtherUserNicknameByUserId(long userId) {
        if (fromUser.getId().equals(userId)) {
            return toUser.getNickname();
        }

        if (toUser.getId().equals(userId)) {
            return fromUser.getNickname();
        }

        throw new RuntimeException();
    }

    public String getAliasByUserId(long userId) {
        if (fromUser.getId().equals(userId)) {
            return fromUserAlias;
        }

        if (toUser.getId().equals(userId)) {
            return toUserAlias;
        }

        throw new RuntimeException();
    }
}
