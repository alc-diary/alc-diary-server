package com.alc.diary.domain.user;

import com.alc.diary.domain.BaseEntity;
import com.alc.diary.domain.exception.DomainException;
import com.alc.diary.domain.user.enums.*;
import com.alc.diary.domain.user.error.UserError;
import lombok.*;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Getter
@ToString(exclude = "detail")
@Builder(builderMethodName = "innerBuilder")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Where(clause = "status = 'ACTIVE'")
@Table(
        name = "users",
        indexes = {@Index(name = "idx_users_social_id", columnList = "social_id")})
@Entity
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(mappedBy = "user")
    private UserDetail detail;

    @Column(name = "social_type", length = 20, nullable = false, updatable = false)
    @Enumerated(EnumType.STRING)
    private SocialType socialType;

    @Column(name = "social_id", length = 50, nullable = false, updatable = false)
    private String socialId;

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

    @Column(name = "profile_image", length = 1024)
    private String profileImage;
    
    public User(
            Long id,
            UserDetail detail,
            SocialType socialType,
            String socialId,
            UserStatus status,
            String email,
            GenderType gender,
            AgeRangeType ageRange,
            String profileImage
    ) {
        if (StringUtils.length(profileImage) > 1000) {
            throw new DomainException(UserError.IMAGE_URL_LENGTH_EXCEEDED);
        }
        this.id = id;
        this.detail = detail;
        this.socialType = socialType;
        this.socialId = socialId;
        this.status = status;
        this.email = email;
        this.gender = gender;
        this.ageRange = ageRange;
        this.profileImage = profileImage;
    }

    public static UserBuilder builder(
            SocialType socialType,
            String socialId
    ) {
        return innerBuilder()
                .socialType(socialType)
                .socialId(socialId)
                .status(UserStatus.ONBOARDING);
    }

    public void setDetail(UserDetail detail) {
        this.detail = detail;
    }

    public void updateProfileImage(String newProfileImage) {
        if (StringUtils.length(newProfileImage) > 1000) {
            throw new DomainException(UserError.IMAGE_URL_LENGTH_EXCEEDED);
        }
        this.profileImage = newProfileImage;
    }

    public void updateAlcoholLimitAndGoal(
            float newPersonalAlcoholLimit,
            int newNonAlcoholGoal,
            AlcoholType newAlcoholType
    ) {
        detail.updateAlcoholLimitAndGoal(newPersonalAlcoholLimit, newNonAlcoholGoal, newAlcoholType);
    }

    public boolean isOnboarding() {
        return status == UserStatus.ONBOARDING;
    }

    public void onboarding(UserDetail detail) {
        detail.setUser(this);
        this.detail = detail;
        this.status = UserStatus.ACTIVE;
    }

    public void updateNickname(String newNickname) {
        detail.updateNickname(newNickname);
    }

    public void delete() {
        status = UserStatus.DEACTIVATED;
    }

    public String getNickname() {
        return detail.getNickname();
    }
}
