package com.alc.diary.domain.user;

import com.alc.diary.domain.BaseEntity;
import com.alc.diary.domain.exception.DomainException;
import com.alc.diary.domain.user.enums.*;
import com.alc.diary.domain.user.error.UserError;
import lombok.*;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@ToString(exclude = "detail")
@Builder(builderMethodName = "innerBuilder")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "users")
@Entity
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY)
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
    
    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    public User(
            Long id,
            UserDetail detail,
            SocialType socialType,
            String socialId,
            UserStatus status,
            String email,
            GenderType gender,
            AgeRangeType ageRange,
            String profileImage,
            LocalDateTime deletedAt
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
        this.deletedAt = deletedAt;
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

    public void onboarding(UserDetail detail) {
        detail.setUser(this);
        this.detail = detail;
    }

    public void updateNickname(String newNickname) {
        detail.updateNickname(newNickname);
    }

    public void delete() {
        deletedAt = LocalDateTime.now();
    }

    public String getNickname() {
        return detail.getNickname();
    }
}
