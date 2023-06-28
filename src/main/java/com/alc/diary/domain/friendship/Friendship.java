package com.alc.diary.domain.friendship;

import com.alc.diary.domain.exception.DomainException;
import com.alc.diary.domain.friendship.error.FriendError;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "friends")
@Entity
public class Friendship {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_a_id", nullable = false, updatable = false)
    private long userAId;

    @Column(name = "user_b_label_by_user_a", length = 30)
    private String userBLabelByUserA;

    @Column(name = "user_b_id", nullable = false, updatable = false)
    private long userBId;

    @Column(name = "user_a_label_by_user_b", length = 30)
    private String userALabelByUserB;

    private Friendship(
            Long userAId,
            String userBLabelByUserA,
            Long userBId,
            String userALabelByUserB
    ) {
        if (userAId == null) {
            throw new DomainException(FriendError.USER_ID_NULL);
        }
        if (userBId == null) {
            throw new DomainException(FriendError.USER_ID_NULL);
        }
        if (StringUtils.length(userBLabelByUserA) > 100) {
            throw new DomainException(FriendError.FRIEND_LABEL_EXCEEDED);
        }
        if (StringUtils.length(userALabelByUserB) > 100) {
            throw new DomainException(FriendError.FRIEND_LABEL_EXCEEDED);
        }
        this.userAId = userAId;
        this.userBLabelByUserA = userBLabelByUserA;
        this.userBId = userBId;
        this.userALabelByUserB = userALabelByUserB;
    }

    public static Friendship create(
            Long userAId,
            String userBLabelByUserA,
            Long userBId,
            String userALabelByUserB
    ) {
        return new Friendship(
                userAId,
                userBLabelByUserA,
                userBId,
                userALabelByUserB
        );
    }

    public long getFriendUserId(long userId) {
        if (userAId == userId) {
            return userBId;
        }
        if (userBId == userId) {
            return userAId;
        }
        throw new DomainException(FriendError.NO_PERMISSION, "User ID: " + userId);
    }

    public String getFriendUserLabel(long userId) {
        if (userAId == userId) {
            return userBLabelByUserA;
        }
        if (userBId == userId) {
            return userALabelByUserB;
        }
        throw new DomainException(FriendError.NO_PERMISSION, "User ID: " + userId);
    }

    public void updateFriendLabel(long userId, String newLabel) {
        if (userAId == userId) {
            userBLabelByUserA = newLabel;
        } else if (userBId == userId) {
            userALabelByUserB = newLabel;
        } else {
            throw new DomainException(FriendError.NO_PERMISSION, "USER ID: " + userId);
        }
    }
}
