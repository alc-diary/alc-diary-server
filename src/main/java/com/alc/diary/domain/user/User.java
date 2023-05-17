package com.alc.diary.domain.user;

import com.alc.diary.domain.BaseEntity;
import com.alc.diary.domain.exception.DomainException;
import com.alc.diary.domain.user.enums.*;
import com.alc.diary.domain.user.error.UserError;
import lombok.*;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Getter
@ToString
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
            throw new DomainException(UserError.IMAGE_URL_LENGTH_EXCEEDED);
        }
        this.profileImage = newProfileImage;
    }

    public void updateAlcoholLimitAndGoal(float newPersonalAlcoholLimit, int newNonAlcoholGoal, AlcoholType newAlcoholType) {
        if (newPersonalAlcoholLimit < 0.0f) {
            throw new DomainException(UserError.INVALID_PERSONAL_ALCOHOL_LIMIT);
        }
        if (newNonAlcoholGoal < 0) {
            throw new DomainException(UserError.INVALID_NON_ALCOHOL_GOAL);
        }
        if (newAlcoholType == null) {
            throw new DomainException(UserError.INVALID_ALCOHOL_TYPE);
        }
        this.personalAlcoholLimit = newPersonalAlcoholLimit;
        this.nonAlcoholGoal = newNonAlcoholGoal;
        this.alcoholType = newAlcoholType;
    }

    public void updateNickname(String newNickname) {
        if (newNickname.length() > 16) {
            throw new DomainException(UserError.NICKNAME_LENGTH_EXCEEDED);
        }
        nickname = newNickname;
    }

    public void updateDescriptionStyle(DescriptionStyle newDescriptionStyle) {
        if (newDescriptionStyle == null) {
            throw new DomainException(UserError.INVALID_DESCRIPTION_STYLE);
        }
        this.descriptionStyle = newDescriptionStyle;
    }
}
