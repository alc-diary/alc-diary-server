package com.alc.diary.application.user;

import com.alc.diary.domain.user.User;
import com.alc.diary.domain.user.enums.AlcoholType;
import com.alc.diary.domain.user.enums.DescriptionStyle;
import com.alc.diary.domain.user.enums.SocialType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.alc.diary.domain.user.enums.AlcoholType.BEER;
import static com.alc.diary.domain.user.enums.AlcoholType.SOJU;
import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class UserAppServiceTest {

    private User user;

    @BeforeEach
    void setUp() {
        user = User.builder(SocialType.KAKAO, "1", DescriptionStyle.MILD)
                   .nickname("test")
                   .personalAlcoholLimit(1.5f)
                   .nonAlcoholGoal(5)
                   .alcoholType(SOJU)
                   .build();
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
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주량과_금주목표를_변경한다() {
        float newPersonalAlcoholLimit = 2.5f;
        int newNonAlcoholGoal = 10;
        AlcoholType newAlcoholType = BEER;

        user.updateAlcoholLimitAndGoal(newPersonalAlcoholLimit, newNonAlcoholGoal, newAlcoholType);

        assertThat(user.getPersonalAlcoholLimit())
                .isEqualTo(newPersonalAlcoholLimit);
        assertThat(user.getNonAlcoholGoal())
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
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주량은 0이상이어야 합니다.");
    }

    @Test
    void 금주_목표_일_수가_0보다_작으면_IllegalArgumentException이_발생한다() {
        int newNonAlcoholGoal = -1;

        assertThatThrownBy(() -> user.updateAlcoholLimitAndGoal(1.0f, newNonAlcoholGoal, BEER))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("금주 목표 일 수는 0이상이어야 합니다.");
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
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("닉네임은 16자를 초과할 수 없습니다.");
    }
}
