package com.example.alcdiary.infrastructure.entity.user;

import com.example.alcdiary.domain.model.UserModel;
import com.example.alcdiary.infrastructure.entity.BaseEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "USER")
@Entity
public class User extends BaseEntity {

    @Id
    private String id;

    @Column(name = "email")
    private String email;

    @Column(name = "nickname")
    private String nickname;

    @Column(name = "profile_image_url")
    private String profileImageUrl;

    @Column(name = "gender")
    private UserModel.Gender gender;

    @Builder
    private User(
            String id,
            String email,
            String nickname,
            String profileImageUrl,
            UserModel.Gender gender,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        this.id = id;
        this.email = email;
        this.nickname = nickname;
        this.profileImageUrl = profileImageUrl;
        this.gender = gender;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static User from(UserModel userModel) {
        return User.builder()
                .id(userModel.getId())
                .email(userModel.getEmail())
                .nickname(userModel.getNickname())
                .profileImageUrl(userModel.getProfileImageUrl())
                .gender(userModel.getGender())
                .createdAt(userModel.getCreatedAt())
                .updatedAt(userModel.getUpdatedAt())
                .build();
    }

    public UserModel convertToDomainModel() {
        return UserModel.builder()
                .id(id)
                .email(email)
                .nickname(nickname)
                .profileImageUrl(profileImageUrl)
                .gender(gender)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();
    }
}
