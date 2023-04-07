package com.alc.diary.domain.user;

import com.alc.diary.domain.BaseEntity;
import com.alc.diary.domain.exception.DomainException;
import com.alc.diary.domain.user.error.UserError;
import com.alc.diary.domain.user.enums.AgeRangeType;
import com.alc.diary.domain.user.enums.DescriptionStyle;
import com.alc.diary.domain.user.enums.GenderType;
import com.alc.diary.domain.user.enums.SocialType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

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

    @Column(name = "nickname", length = 14)
    private String nickname;

    @Column(name = "social_type", length = 20, nullable = false, updatable = false)
    @Enumerated(EnumType.STRING)
    private SocialType socialType;

    @Column(name = "social_id", length = 50, nullable = false, updatable = false)
    private String socialId;

    @Column(name = "drink_amount")
    private int drinkAmount;

    @Column(name = "non_alcohol_goal")
    private int nonAlcoholGoal;

    @Column(name = "description_style", length = 20, nullable = false)
    @Enumerated(EnumType.STRING)
    private DescriptionStyle descriptionStyle;

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
                .descriptionStyle(descriptionStyle);
    }

    public User(
        String nickname,
        SocialType socialType,
        String socialId,
        int drinkAmount,
        int nonAlcoholGoal,
        DescriptionStyle descriptionStyle,
        String email,
        GenderType gender,
        AgeRangeType ageRange,
        String profileImage
    ) {
        if (!StringUtils.hasText(nickname)) {
            throw new DomainException(UserError.INVALID_PARAMETER_INCLUDE);
        }
        if (socialType == null) {
            throw new DomainException(UserError.INVALID_PARAMETER_INCLUDE);
        }
        if (descriptionStyle == null) {
            throw new DomainException(UserError.INVALID_PARAMETER_INCLUDE);
        }
        this.nickname = nickname;
        this.socialType = socialType;
        this.socialId = socialId;
        this.drinkAmount = drinkAmount;
        this.nonAlcoholGoal = nonAlcoholGoal;
        this.descriptionStyle = descriptionStyle;
        this.email = email;
        this.gender = gender;
        this.ageRange = ageRange;
        this.profileImage = profileImage;
    }
}
