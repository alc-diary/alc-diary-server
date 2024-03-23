package com.alc.diary.domain.user;

import com.alc.diary.domain.BaseCreationEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "user_group_memberships")
@Entity
public class UserGroupMembership extends BaseCreationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_group_id", nullable = false)
    private UserGroup userGroup;

    private UserGroupMembership(User user, UserGroup userGroup) {
        this.user = user;
        this.userGroup = userGroup;
    }

    public static UserGroupMembership create(User user, UserGroup userGroup) {
        return new UserGroupMembership(user, userGroup);
    }
}
