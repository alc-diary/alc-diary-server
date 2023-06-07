package com.alc.diary.domain.user;

import com.alc.diary.domain.user.enums.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
        name = "user_histories",
        indexes = {@Index(name = "idx_user_histories_user_id_change_timestamp", columnList = "user_id,change_timestamp")}
)
@Entity
public class UserHistory {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false, updatable = false)
    private long userId;

    @Column(name = "social_type", length = 20, nullable = false, updatable = false)
    @Enumerated(EnumType.STRING)
    private SocialType socialType;

    @Column(name = "social_id", length = 50, nullable = false, updatable = false)
    private String socialId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20, updatable = false)
    private UserStatus status;

    @Column(name = "email", length = 100, updatable = false)
    private String email;

    @Column(name = "gender", length = 20, updatable = false)
    @Enumerated(EnumType.STRING)
    private GenderType gender;

    @Column(name = "age_range", length = 20, updatable = false)
    @Enumerated(EnumType.STRING)
    private AgeRangeType ageRange;

    @Column(name = "profile_image", length = 1024, updatable = false)
    private String profileImage;

    @Column(name = "nickname", length = 16, updatable = false)
    private String nickname;

    @Enumerated(EnumType.STRING)
    @Column(name = "alcohol_type", length = 20, updatable = false)
    private AlcoholType alcoholType;

    @Column(name = "personal_alcohol_limit", updatable = false)
    private float personalAlcoholLimit;

    @Column(name = "non_alcohol_goal", updatable = false)
    private int nonAlcoholGoal;

    @Enumerated(EnumType.STRING)
    @Column(name = "description_style", length = 20, nullable = false)
    private DescriptionStyle descriptionStyle;

    @Column(name = "deleted_at", updatable = false)
    private LocalDateTime deletedAt;

    @Column(name = "changed_by", updatable = false)
    private long changedBy;

    @Column(name = "change_timestamp", updatable = false)
    private LocalDateTime changeTimestamp;

    public UserHistory(
            Long id,
            long userId,
            SocialType socialType,
            String socialId,
            UserStatus status,
            String email,
            GenderType gender,
            AgeRangeType ageRange,
            String profileImage,
            String nickname,
            AlcoholType alcoholType,
            float personalAlcoholLimit,
            int nonAlcoholGoal,
            DescriptionStyle descriptionStyle,
            LocalDateTime deletedAt,
            long changedBy,
            LocalDateTime changeTimestamp
    ) {
        this.id = id;
        this.userId = userId;
        this.socialType = socialType;
        this.socialId = socialId;
        this.status = status;
        this.email = email;
        this.gender = gender;
        this.ageRange = ageRange;
        this.profileImage = profileImage;
        this.nickname = nickname;
        this.alcoholType = alcoholType;
        this.personalAlcoholLimit = personalAlcoholLimit;
        this.nonAlcoholGoal = nonAlcoholGoal;
        this.descriptionStyle = descriptionStyle;
        this.deletedAt = deletedAt;
        this.changedBy = changedBy;
        this.changeTimestamp = changeTimestamp;
    }

    public static UserHistory from(Long requesterId, User target) {
        return new UserHistory(
                null,
                target.getId(),
                target.getSocialType(),
                target.getSocialId(),
                target.getStatus(),
                target.getEmail(),
                target.getGender(),
                target.getAgeRange(),
                target.getProfileImage(),
                target.getDetail() != null
                        ? target.getDetail().getNickname()
                        : null,
                target.getDetail() != null
                        ? target.getDetail().getAlcoholType()
                        : null,
                target.getDetail() != null
                        ? target.getDetail().getPersonalAlcoholLimit()
                        : 0.0f,
                target.getDetail() != null
                        ? target.getDetail().getNonAlcoholGoal()
                        : 0,
                target.getDetail() != null
                        ? target.getDetail().getDescriptionStyle()
                        : null,
                target.getDeletedAt(),
                requesterId,
                LocalDateTime.now()
        );
    }
}
