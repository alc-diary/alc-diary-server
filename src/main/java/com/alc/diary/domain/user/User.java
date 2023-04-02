package com.alc.diary.domain.user;

import com.alc.diary.domain.exception.DomainException;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nickname")
    private String nickname;

    @Column(name = "social_type", updatable = false)
    private String socialType;

    @Column(name = "drink_amount")
    private int drinkAmount;
    @Column(name = "non_alcohol_goal")
    private String nonAlcoholGoal;
    @Column(name = "description_style")
    private String descriptionStyle;

    @Column(name = "email", updatable = false)
    private String email;

    @Column(name = "gender", updatable = false)
    private String gender;

    @Column(name = "age_range", updatable = false)
    private String ageRange;

    @Column(name = "profile_image")
    private String profileImage;

    @Builder
    public User(String nickname, String socialType, int drinkAmount, String nonAlcoholGoal, String descriptionStyle) {
        if (!StringUtils.hasText(nickname) || !StringUtils.hasText(socialType) || !StringUtils.hasText(nonAlcoholGoal) || !StringUtils.hasText(descriptionStyle)) {
            throw new DomainException();
        }
        this.nickname = nickname;
        this.socialType = socialType;
        this.drinkAmount = drinkAmount;
        this.nonAlcoholGoal = nonAlcoholGoal;
        this.descriptionStyle = descriptionStyle;
    }
}
