package com.example.alcdiary.infrastructure.entity;

import com.example.alcdiary.domain.model.user.*;
import lombok.*;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "user")
@Entity
public class User extends BaseEntity {

    @Column(name = "id")
    @Id
    private String id;

    @Column(name = "email")
    private String email;

    @Column(name = "nickname")
    private String nickname;

    @Column(name = "profile_image_url")
    private String profileImageUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "theme")
    private EUserTheme theme;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    private EUserGender gender;

    @Enumerated(EnumType.STRING)
    @Column(name = "age_group")
    private EUserAgeGroup ageGroup;

    @Enumerated(EnumType.STRING)
    @Column(name = "alcohol_type")
    private EUserAlcoholType alcoholType;

    @Column(name = "drink_capacity")
    private int drinkCapacity;

    @Column(name = "resolution_days")
    private int resolutionDays;

    public UserModel toDomainModel() {
        return UserModel.builder()
                .id(UserIdModel.fromString(id))
                .email(email)
                .nickname(nickname)
                .profileImageUrl(profileImageUrl)
                .theme(theme)
                .gender(gender)
                .ageGroup(ageGroup)
                .drinkingCapacityModel(new UserDrinkingCapacityModel(alcoholType, drinkCapacity))
                .promise(new UserPromise(resolutionDays))
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();
    }

    public static User fromDomainModel(UserModel userModel) {
        return User.builder()
                .id(userModel.getId().parse())
                .email(userModel.getEmail())
                .nickname(userModel.getNickname())
                .profileImageUrl(userModel.getProfileImageUrl())
                .theme(userModel.getTheme())
                .gender(userModel.getGender())
                .ageGroup(userModel.getAgeGroup())
                .alcoholType(userModel.getDrinkingCapacityModel().getAlcoholType())
                .drinkCapacity(userModel.getDrinkingCapacityModel().getCapacity())
                .resolutionDays(userModel.getPromise().getResolutionDays())
                .build();
    }
}
