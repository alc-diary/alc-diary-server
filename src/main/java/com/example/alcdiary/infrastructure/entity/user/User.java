package com.example.alcdiary.infrastructure.entity.user;

import com.example.alcdiary.domain.model.UserModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@IdClass(UserPk.class)
@Table(name = "USER")
@Entity
public class User {

    @Id
    private Long id;

    @Id
    @Enumerated(EnumType.STRING)
    @Column(name = "social_type", nullable = false, updatable = false)
    private UserModel.SocialType socialType;

    @Column(name = "email")
    private String email;

    @Column(name = "nickname")
    private String nickname;

    @Column(name = "profile_image_url")
    private String profileImageUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    private UserModel.Gender gender;

    @Column(name = "refresh_token")
    private String refreshToken;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
