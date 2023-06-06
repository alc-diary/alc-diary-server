package com.alc.diary.domain.user;

import com.alc.diary.domain.BaseEntity;
import com.alc.diary.domain.exception.DomainException;
import com.alc.diary.domain.user.enums.AlcoholType;
import com.alc.diary.domain.user.enums.DescriptionStyle;
import com.alc.diary.domain.user.error.UserError;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;

@Getter
@ToString
@NoArgsConstructor
@Table(name = "user_details")
@Entity
public class UserDetail extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "nickname", length = 16, unique = true)
    private String nickname;

    @Enumerated(EnumType.STRING)
    @Column(name = "alcohol_type", length = 20)
    private AlcoholType alcoholType;

    @Column(name = "personal_alcohol_limit")
    private float personalAlcoholLimit;

    @Column(name = "non_alcohol_goal")
    private int nonAlcoholGoal;

    @Enumerated(EnumType.STRING)
    @Column(name = "description_style", length = 20, nullable = false)
    private DescriptionStyle descriptionStyle;

    public UserDetail(
            Long id,
            User user,
            String nickname,
            AlcoholType alcoholType,
            float personalAlcoholLimit,
            int nonAlcoholGoal,
            DescriptionStyle descriptionStyle
    ) {
        if (user == null) {
            throw new DomainException(UserError.INVALID_PARAMETER_INCLUDE);
        }
        if (StringUtils.isEmpty(nickname) || nickname.length() > 16) {
            throw new DomainException(UserError.INVALID_PARAMETER_INCLUDE);
        }
        if (alcoholType == null) {
            throw new DomainException(UserError.INVALID_PARAMETER_INCLUDE);
        }
        if (personalAlcoholLimit < 0.0f) {
            throw new DomainException(UserError.INVALID_PERSONAL_ALCOHOL_LIMIT);
        }
        if (nonAlcoholGoal > 7 || nonAlcoholGoal < 0) {
            throw new DomainException(UserError.INVALID_NON_ALCOHOL_GOAL);
        }
        if (descriptionStyle == null) {
            throw new DomainException(UserError.INVALID_PARAMETER_INCLUDE);
        }
        this.id = id;
        this.user = user;
        this.nickname = nickname;
        this.alcoholType = alcoholType;
        this.personalAlcoholLimit = personalAlcoholLimit;
        this.nonAlcoholGoal = nonAlcoholGoal;
        this.descriptionStyle = descriptionStyle;
    }

    public void updateAlcoholLimitAndGoal(float newPersonalAlcoholLimit, int newNonAlcoholGoal, AlcoholType newAlcoholType) {
        if (newPersonalAlcoholLimit < 0.0f) {
            throw new DomainException(UserError.INVALID_PERSONAL_ALCOHOL_LIMIT);
        }
        if (newNonAlcoholGoal > 7 || newNonAlcoholGoal < 0) {
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

    public void update(
            DescriptionStyle newDescriptionStyle,
            String newNickname,
            AlcoholType newAlcoholType,
            float newPersonalAlcoholLimit,
            int newNonAlcoholGoal
    ) {
        descriptionStyle = newDescriptionStyle;
        nickname = newNickname;
        alcoholType = newAlcoholType;
        personalAlcoholLimit = newPersonalAlcoholLimit;
        nonAlcoholGoal = newNonAlcoholGoal;
    }
}
