package com.alc.diary.domain.user;

import com.alc.diary.domain.BaseEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "user_groups")
@Entity
public class UserGroup extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @OneToMany(mappedBy = "userGroup", cascade = CascadeType.PERSIST)
    private Set<UserGroupMembership> memberships = new HashSet<>();

    private UserGroup(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public static UserGroup create(String name, String description) {
        return new UserGroup(name, description);
    }

    public void addUser(User user) {
        UserGroupMembership membership = UserGroupMembership.create(user, this);
        this.memberships.add(membership);
    }
}
