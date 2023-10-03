package com.alc.diary.domain.user;

import com.alc.diary.domain.exception.DomainException;
import com.alc.diary.domain.user.enums.*;
import com.alc.diary.domain.user.error.UserError;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class UserTest {

    private User user;
    private User onboardingUser;
    private UserDetail mockUserDetail;

    @BeforeEach
    void setUp() {
        user = User.builder(SocialType.KAKAO, "social-id1")
                .id(1L)
                .email("email1")
                .gender(GenderType.MALE)
                .ageRange(AgeRangeType.THIRTIES)
                .profileImage("profile image1")
                .build();
        mockUserDetail = mock(UserDetail.class);
        user.onboarding(mockUserDetail);

        onboardingUser = User.builder(SocialType.GOOGLE, "social-id2")
                .id(2L)
                .email("email2")
                .gender(GenderType.FEMALE)
                .ageRange(AgeRangeType.TWENTIES)
                .profileImage("profile image2")
                .build();
    }

    @DisplayName("isOnboarding 메서드를 호출할 때, User의 status가 ONBOARDING이면 true를 반환한다.")
    @Test
    void givenUserStatusIsActive_whenIsOnboardingCalled_thenReturnTrue() {
        // given
        // when & then
        assertThat(onboardingUser.isOnboarding()).isTrue();
    }

    @DisplayName("isOnboarding 메서드를 호출할 때, User의 status가 ACTIVE이면 false를 반환한다.")
    @Test
    void givenUserStatusIsActive_whenIsOnboardingCalled_thenReturnFalse() {
        // given
        UserDetail userDetail = mock(UserDetail.class);

        // when
        onboardingUser.onboarding(userDetail);

        // then
        assertThat(user.isOnboarding()).isFalse();
        assertThat(onboardingUser.isOnboarding()).isFalse();
    }

    @DisplayName("url이 1000자 이하이면 profileImage가 업데이트된다.")
    @Test
    void shouldUpdateProfileImageWhenUrlLengthIsLessOrEqualThan1000() {
        // given
        String newProfileImage = StringUtils.repeat("a", 1000);

        // when
        user.updateProfileImage(newProfileImage);

        // then
        assertThat(user.getProfileImage()).isEqualTo(newProfileImage);
    }

    @DisplayName("url이 1000자를 초과하면 예외가 발생한다.")
    @Test
    void shouldThrowExceptionWhenUrlLengthIsGreaterThan1000() {
        // given
        String newProfileImage = StringUtils.repeat("a", 1001);

        // when
        assertThatThrownBy(() -> user.updateProfileImage(newProfileImage))
                .isInstanceOf(DomainException.class)
                .hasMessage("프로필 이미지 URL은 1000자를 초과할 수 없습니다.");
    }

    @DisplayName("AlcoholLimit과 Goal이 업데이트 된다.")
    @Test
    void shouldUpdateAlcoholLimitAndGoal() {
        // given
        float newPersonalAlcoholLimit = 5.5f;
        int newNonAlcoholGoal = 4;
        AlcoholType newAlcoholType = AlcoholType.BEER;

        // when
        user.updateAlcoholLimitAndGoal(newPersonalAlcoholLimit, newNonAlcoholGoal, newAlcoholType);

        // then
        verify(mockUserDetail).updateAlcoholLimitAndGoal(newPersonalAlcoholLimit, newNonAlcoholGoal, newAlcoholType);
    }

    @DisplayName("잘못된 personalAlcoholLimit이 주어졌을 때 예외가 발생한다.")
    @Test
    void shouldThrowExceptionWhenInvalidPersonalAlcoholLimitGiven() {
        // given
        float newPersonalAlcoholLimit = -1.0f;
        int newNonAlcoholGoal = 4;
        AlcoholType newAlcoholType = AlcoholType.BEER;

        doThrow(new DomainException(UserError.INVALID_PERSONAL_ALCOHOL_LIMIT))
                .when(mockUserDetail).updateAlcoholLimitAndGoal(newPersonalAlcoholLimit, newNonAlcoholGoal, newAlcoholType);

        // when
        assertThatThrownBy(() -> user.updateAlcoholLimitAndGoal(newPersonalAlcoholLimit, newNonAlcoholGoal, newAlcoholType))
                .isInstanceOf(DomainException.class)
                .hasMessage("주량은 0 이상이어야 합니다.");
    }

    @DisplayName("User가 활성화 상태일 때 deactivate 메서드가 호출되면 닉네임이 수정되고 DEACTIVATED 상태가 된다.")
    @Test
    void givenUserIsActive_whenDeactivateCalled_thenNicknameShouldUpdateAndStatusShouldBeDeactivated() {
        // given
        String deactivateNickname = "탈퇴한유저asdfas";

        // when
        user.deactivate(deactivateNickname);

        // then
        verify(mockUserDetail).updateNickname(deactivateNickname);
        assertThat(user.getStatus()).isEqualTo(UserStatus.DEACTIVATED);
    }
}