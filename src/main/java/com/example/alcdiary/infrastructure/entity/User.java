package com.example.alcdiary.infrastructure.entity;

import com.example.alcdiary.domain.model.user.*;
import lombok.*;

import javax.persistence.*;

@Getter
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
    @Column(name = "age_range")
    private EUserAgeRange ageRange;

    @Enumerated(EnumType.STRING)
    @Column(name = "alcohol_type")
    private EUserAlcoholType alcoholType;

    @Column(name = "drink_capacity")
    private int drinkCapacity;

    @Column(name = "resolution_days")
    private int resolutionDays;

    protected User() {
    }

    public User(
            String id,
            String email,
            String nickname,
            String profileImageUrl,
            EUserTheme theme,
            EUserGender gender,
            EUserAgeRange ageGroup,
            EUserAlcoholType alcoholType,
            int drinkCapacity,
            int resolutionDays
    ) {
        this.id = id;
        this.email = email;
        this.nickname = nickname;
        this.profileImageUrl = profileImageUrl;
        this.theme = theme;
        this.gender = gender;
        this.ageRange = ageGroup;
        this.alcoholType = alcoholType;
        this.drinkCapacity = drinkCapacity;
        this.resolutionDays = resolutionDays;
    }

    public UserModel convertToDomainModel() {
        return UserModel.of(
                UserIdModel.from(this.id),
                this.email,
                this.nickname,
                this.profileImageUrl,
                this.theme,
                this.gender,
                this.ageRange,
                UserDrinkingCapacityModel.of(this.alcoholType, this.drinkCapacity),
                UserPromiseModel.from(this.resolutionDays),
                this.createdAt,
                this.updatedAt
        );
    }

    public static User from(UserModel userModel) {
        EUserAlcoholType alcoholType = null;
        int drinkCapacity = 0;
        int resolutionDays = 0;
        if (userModel.getDrinkingCapacityModel() != null) {
            alcoholType = userModel.getDrinkingCapacityModel().getAlcoholType();
            drinkCapacity = userModel.getDrinkingCapacityModel().getCapacity();
        }
        if (userModel.getPromise() != null) {
            resolutionDays = userModel.getPromise().getResolutionDays();
        }
        return new User(
                userModel.getId().parse(),
                userModel.getEmail(),
                userModel.getProfileImageUrl(),
                userModel.getProfileImageUrl(),
                userModel.getTheme(),
                userModel.getGender(),
                userModel.getAgeRange(),
                alcoholType,
                drinkCapacity,
                resolutionDays
        );
    }
}
