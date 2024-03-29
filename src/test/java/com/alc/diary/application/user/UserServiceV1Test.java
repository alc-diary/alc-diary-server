package com.alc.diary.application.user;

import com.alc.diary.domain.exception.DomainException;
import com.alc.diary.domain.user.User;
import com.alc.diary.domain.user.UserDetail;
import com.alc.diary.domain.user.enums.AlcoholType;
import com.alc.diary.domain.user.enums.DescriptionStyle;
import com.alc.diary.domain.user.enums.SocialType;
import com.alc.diary.domain.user.error.UserError;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.alc.diary.domain.user.enums.AlcoholType.BEER;
import static com.alc.diary.domain.user.enums.AlcoholType.SOJU;
import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class UserServiceV1Test {

    private User user;

    @BeforeEach
    void setUp() {
        user = User.builder(SocialType.KAKAO, "1")
                   .id(1L)
                   .build();
        UserDetail userDetail = new UserDetail(
                1L,
                user,
                "test",
                SOJU,
                1.5f,
                5,
                DescriptionStyle.MILD
        );
        user.setDetail(userDetail);
    }

    @Test
    void 프로필_이미지를_변경한다() {
        String expect = "new_profile_image_url";
        user.updateProfileImage(expect);
        assertThat(user.getProfileImage())
                .isEqualTo(expect);
    }

    @Test
    void 프로필_이미지_URL이_1000자를_초과하면_IllegalArgumentException이_발생한다() {
        StringBuilder newProfileImage = new StringBuilder(2000);
        // 1100자 String 생성
        for (int i = 0; i < 100; i++) {
            newProfileImage.append("aaaaaaaaaaa");
        }
        assertThatThrownBy(() -> user.updateProfileImage(newProfileImage.toString()))
                .isInstanceOf(DomainException.class)
                .hasMessage("프로필 이미지 URL은 1000자를 초과할 수 없습니다.");
    }

    @Test
    void 주량과_금주목표를_변경한다() {
        float newPersonalAlcoholLimit = 2.5f;
        int newNonAlcoholGoal = 6;
        AlcoholType newAlcoholType = BEER;

        user.updateAlcoholLimitAndGoal(newPersonalAlcoholLimit, newNonAlcoholGoal, newAlcoholType);

        assertThat(user.getDetail().getPersonalAlcoholLimit())
                .isEqualTo(newPersonalAlcoholLimit);
        assertThat(user.getDetail().getNonAlcoholGoal())
                .isEqualTo(newNonAlcoholGoal);
    }

    @Test
    void 주량이_0보다_작으면_IllegalArgumentException이_발생한다() {
        float newPersonalAlcoholLimit = -0.5f;

        assertThatThrownBy(() -> user.updateAlcoholLimitAndGoal(
                newPersonalAlcoholLimit,
                10,
                BEER
        ))
                .isInstanceOf(DomainException.class)
                .hasMessage("주량은 0 이상이어야 합니다.");
    }

    @Test
    void 금주_목표_일_수가_0보다_작으면_IllegalArgumentException이_발생한다() {
        int newNonAlcoholGoal = -1;

        assertThatThrownBy(() -> user.updateAlcoholLimitAndGoal(1.0f, newNonAlcoholGoal, BEER))
                .isInstanceOf(DomainException.class)
                .hasMessage("금주 목표일 수는 0 이상 7 이하여야 합니다.");
    }

    @Test
    void 닉네임을_변경한다() {
        String expect = "abc";
        user.updateNickname(expect);
        assertThat(user.getNickname())
                .isEqualTo(expect);
    }

    @Test
    void 닉네임이_16자를_초과하면_IllegalArgumentException이_발생한다() {
        String newNickname = "abcdeabcdeabcdeabcde";
        assertThatThrownBy(() -> user.updateNickname(newNickname))
                .isInstanceOf(DomainException.class)
                .hasMessage("닉네임은 16자를 초과할 수 없습니다.");
    }
}
