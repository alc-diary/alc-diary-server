package com.example.alcdiary.infrastructure.entity.user;

import com.example.alcdiary.domain.model.UserModel;
import com.example.alcdiary.infrastructure.entity.BaseEntity;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "KAKAO_USER")
@Entity
public class KakaoUser extends BaseEntity {

    @Column(name = "id")
    @Id
    private Long id;

    @Column(name = "email")
    private String email;

    @Column(name = "profile_image_url")
    private String profileImageUrl;

    @Column(name = "gender")
    private UserModel.Gender gender;
}
