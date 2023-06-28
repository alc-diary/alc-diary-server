package com.alc.diary.domain.friendship;

import com.alc.diary.domain.exception.DomainException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "friends")
@Entity
public class Friend {

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

    private long friendRequestId;

    private Friend(
            Long userAId,
            String userBLabelByUserA,
            Long userBId,
            String userALabelByUserB,
            Long friendRequestId
    ) {
        if (userAId == null) {
            throw new DomainException();
        }
        if (userBId == null) {
            throw new DomainException();
        }
        if (StringUtils.length(userBLabelByUserA) > 100) {
            throw new DomainException();
        }
        if (StringUtils.length(userALabelByUserB) > 100) {
            throw new DomainException();
        }
        this.userAId = userAId;
        this.userBLabelByUserA = userBLabelByUserA;
        this.userBId = userBId;
        this.userALabelByUserB = userALabelByUserB;
        this.friendRequestId = friendRequestId;
    }

    public static Friend create(
            Long userAId,
            String userBLabelByUserA,
            Long userBId,
            String userALabelByUserB,
            Long friendRequestId
    ) {
        return new Friend(
                userAId,
                userBLabelByUserA,
                userBId,
                userALabelByUserB,
                friendRequestId
        );
    }

    public long getFriendUserId(long userId) {
        if (userAId == userId) {
            return userBId;
        }
        if (userBId == userId) {
            return userAId;
        }
        throw new DomainException();
    }

    public String getFriendUserLabel(long userId) {
        if (userAId == userId) {
            return userBLabelByUserA;
        }
        if (userBId == userId) {
            return userALabelByUserB;
        }
        throw new DomainException();
    }
}
