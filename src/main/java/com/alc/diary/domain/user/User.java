package com.alc.diary.domain.user;

import com.alc.diary.domain.BaseEntity;
import com.alc.diary.domain.exception.DomainException;
import com.alc.diary.domain.user.enums.*;
import com.alc.diary.domain.user.error.UserError;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Builder(builderMethodName = "innerBuilder")
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
@Entity
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nickname", length = 16, unique = true)
    private String nickname;

    @Column(name = "social_type", length = 20, nullable = false, updatable = false)
    @Enumerated(EnumType.STRING)
    private SocialType socialType;

    @Column(name = "social_id", length = 50, nullable = false, updatable = false)
    private String socialId;

    @Column(name = "personal_alcohol_limit")
    private float personalAlcoholLimit;

    @Column(name = "non_alcohol_goal")
    private int nonAlcoholGoal;

    @Column(name = "description_style", length = 20, nullable = false)
    @Enumerated(EnumType.STRING)
    private DescriptionStyle descriptionStyle;

    @Enumerated(EnumType.STRING)
    @Column(name = "alcohol_type", length = 20)
    private AlcoholType alcoholType;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20)
    private UserStatus status;

    @Column(name = "email", length = 100, updatable = false)
    private String email;

    @Column(name = "gender", length = 20, updatable = false)
    @Enumerated(EnumType.STRING)
    private GenderType gender;

    @Column(name = "age_range", length = 20, updatable = false)
    @Enumerated(EnumType.STRING)
    private AgeRangeType ageRange;

    @Column(name = "profile_image", length = 1000)
    private String profileImage;

    public static UserBuilder builder(
        SocialType socialType,
        String socialId,
        DescriptionStyle descriptionStyle
    ) {
        return innerBuilder()
            .socialType(socialType)
            .socialId(socialId)
            .descriptionStyle(descriptionStyle)
            .status(UserStatus.ONBOARDING);
    }

    public void onboarding(
        DescriptionStyle descriptionStyle,
        String nickname,
        AlcoholType alcoholType,
        float personalAlcoholLimit,
        int nonAlcoholGoal
    ) {
        if (descriptionStyle == null) {
            throw new DomainException(UserError.INVALID_PARAMETER_INCLUDE);
        }
        if (nickname == null) {
            throw new DomainException(UserError.INVALID_PARAMETER_INCLUDE);
        }
        if (alcoholType == null) {
            throw new DomainException(UserError.INVALID_PARAMETER_INCLUDE);
        }
        this.descriptionStyle = descriptionStyle;
        this.nickname = nickname;
        this.alcoholType = alcoholType;
        this.personalAlcoholLimit = personalAlcoholLimit;
        this.nonAlcoholGoal = nonAlcoholGoal;
        this.status = UserStatus.ACTIVE;
    }

    public void updateProfileImage(String newProfileImage) {
        if (newProfileImage.length() > 1000) {
            throw new IllegalArgumentException("프로필 이미지 URL은 1000자를 초과할 수 없습니다.");
        }
        this.profileImage = newProfileImage;
    }

    public void updateAlcoholLimitAndGoal(float newPersonalAlcoholLimit, int newNonAlcoholGoal) {
        if (newPersonalAlcoholLimit < 0.0f) {
            throw new IllegalArgumentException("주량은 0이상이어야 합니다.");
        }
        if (newNonAlcoholGoal < 0) {
            throw new IllegalArgumentException("금주 목표 일 수는 0이상이어야 합니다.");
        }
        this.personalAlcoholLimit = newPersonalAlcoholLimit;
        this.nonAlcoholGoal = newNonAlcoholGoal;
    }

    public void updateNickname(String newNickname) {
        if (newNickname.length() > 16) {
            throw new IllegalArgumentException("닉네임은 16자를 초과할 수 없습니다.");
        }
        nickname = newNickname;
    }
}
