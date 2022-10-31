package com.example.alcdiary.infrastructure.entity.user;

import com.example.alcdiary.domain.model.UserModel;
import com.example.alcdiary.infrastructure.entity.BaseEntity;
import lombok.*;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "USER")
@Entity
public class User extends BaseEntity {

    @Id
    private Long id;

    @Column(name = "email")
    private String email;

    @Column(name = "nickname")
    private String nickname;

    @Column(name = "profile_image_url")
    private String profileImageUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    private UserModel.Gender gender;

    @Builder(access = AccessLevel.PRIVATE)
    public User(Long id, String email, String nickname, String profileImageUrl, UserModel.Gender gender) {
        this.id = id;
        this.email = email;
        this.nickname = nickname;
        this.profileImageUrl = profileImageUrl;
        this.gender = gender;
    }

    public static User from(UserModel userModel) {
        return User.builder()
                .id(userModel.getId())
                .email(userModel.getEmail())
                .nickname(userModel.getNickname())
                .profileImageUrl(userModel.getProfileImageUrl())
                .gender(userModel.getGender())
                .build();
    }

    public UserModel toModel() {
        return UserModel.builder()
                .id(id)
                .email(email)
                .nickname(nickname)
                .profileImageUrl(profileImageUrl)
                .gender(gender)
                .socialType(null)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();
    }
}
