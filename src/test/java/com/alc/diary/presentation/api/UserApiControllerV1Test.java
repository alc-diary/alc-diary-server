package com.alc.diary.presentation.api;

import com.alc.diary.application.nickname.NicknameAppService;
import com.alc.diary.application.user.LogoutAppService;
import com.alc.diary.application.user.UserServiceV1;
import com.alc.diary.application.user.dto.request.UpdateAlcoholLimitAndGoalAppRequest;
import com.alc.diary.application.user.dto.request.UpdateDescriptionStyleAppRequest;
import com.alc.diary.application.user.dto.request.UpdateNicknameAppRequest;
import com.alc.diary.application.user.dto.request.UpdateUserProfileImageAppRequest;
import com.alc.diary.application.user.dto.response.GetUserInfoAppResponse;
import com.alc.diary.application.user.dto.response.SearchUserAppResponse;
import com.alc.diary.domain.exception.DomainException;
import com.alc.diary.domain.user.enums.AlcoholType;
import com.alc.diary.domain.user.enums.DescriptionStyle;
import com.alc.diary.domain.user.enums.UserStatus;
import com.alc.diary.domain.user.error.UserError;
import com.alc.diary.presentation.filter.JwtAuthenticationFilter;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(
        value = UserApiControllerV1.class,
        excludeFilters = {@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtAuthenticationFilter.class)})
class UserApiControllerV1Test {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserServiceV1 userServiceV1;

    @MockBean
    private NicknameAppService nicknameAppService;

    @MockBean
    private LogoutAppService logoutAppService;

    @DisplayName("닉네임으로 유저를 검색하면 유저 정보를 응답한다.")
    @Test
    void shouldReturnUserInfoWhenSearchUserByNickname() throws Exception {
        // given
        String nickname = "nickname";
        given(userServiceV1.searchUser(nickname))
                .willReturn(new SearchUserAppResponse(1L, "profile image url", nickname));

        // when & then
        mvc.perform(get("/v1/users")
                        .param("nickname", nickname))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.code").value("S0000"))
                .andExpect(jsonPath("$.message").value("success"))
                .andExpect(jsonPath("$.data.userId").value(1L))
                .andExpect(jsonPath("$.data.profileImage").value("profile image url"))
                .andExpect(jsonPath("$.data.nickname").value(nickname));
        then(userServiceV1).should().searchUser(nickname);
    }

    @DisplayName("null으로 유저를 검색하면 400 응답 코드로 응답한다.")
    @Test
    void shouldReturn400WhenSearchUserByNull() throws Exception {
        // given
        String nickname = null;

        // when & then
        mvc.perform(get("/v1/users")
                        .param("nickname", nickname))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.code").value("E9998"))
                .andExpect(jsonPath("$.message").value("Required request parameter 'nickname' for method parameter type String is not present"));
        then(userServiceV1).shouldHaveNoInteractions();
    }

    @DisplayName("잘못된 형식의 닉네임으로 유저를 검색하면 400 응답 코드로 응답한다.")
    @ParameterizedTest
    @ValueSource(strings = {"a ", " b", "a-", "ab$"})
    void shouldReturn400WhenSearchUserByInvalidNickname(String input) throws Exception {
        // given
        // when & then
        mvc.perform(get("/v1/users")
                        .param("nickname", input))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.code").value("US_E0011"))
                .andExpect(jsonPath("$.message").value("닉네임은 한글, 영어 대소문자, 숫자로만 검색할 수 있습니다."));
        then(userServiceV1).shouldHaveNoInteractions();
    }

    @DisplayName("유저 ID로 유저를 조회한다.")
    @Test
    void shouldReturnUserInfoWhenUserIdIsProvided() throws Exception {
        // given
        long userId = 1L;
        GetUserInfoAppResponse response = new GetUserInfoAppResponse(
                userId,
                DescriptionStyle.MALA,
                AlcoholType.BEER,
                "nickname",
                1.0f,
                3,
                "profile image url",
                UserStatus.ACTIVE
        );
        given(userServiceV1.getUserInfo(userId))
                .willReturn(response);

        // when & then
        mvc.perform(get("/v1/users/info")
                        .contentType(MediaType.APPLICATION_JSON)
                        .requestAttr("userId", userId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.code").value("S0000"))
                .andExpect(jsonPath("$.message").value("success"))
                .andExpect(jsonPath("$.data.userId").value(userId))
                .andExpect(jsonPath("$.data.descriptionStyle").value("MALA"))
                .andExpect(jsonPath("$.data.alcoholType").value("BEER"))
                .andExpect(jsonPath("$.data.nickname").value("nickname"))
                .andExpect(jsonPath("$.data.personalAlcoholLimit").value(1.0f))
                .andExpect(jsonPath("$.data.nonAlcoholGoal").value(3))
                .andExpect(jsonPath("$.data.profileImage").value("profile image url"))
                .andExpect(jsonPath("$.data.status").value("ACTIVE"));
        then(userServiceV1).should().getUserInfo(userId);
    }

    @DisplayName("존재하지 않는 유저 ID로 조회하면 400 응답 코드를 응답한다.")
    @Test
    void shouldReturn400WhenUserIdIsInvalid() throws Exception {
        // given
        long userId = 2L;
        given(userServiceV1.getUserInfo(userId))
                .willThrow((new DomainException(UserError.USER_NOT_FOUND)));

        // when & then
        mvc.perform(get("/v1/users/info")
                        .contentType(MediaType.APPLICATION_JSON)
                        .requestAttr("userId", userId))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.code").value("US_E0000"))
                .andExpect(jsonPath("$.message").value("User not found."));
        then(userServiceV1).should().getUserInfo(userId);
    }

    @DisplayName("프로필 이미지를 업데이트하면 성공 코드를 응답한다.")
    @Test
    void shouldReturnSuccessWhenUpdatingProfileImage() throws Exception {
        // given
        long userId = 1L;
        UpdateUserProfileImageAppRequest request = new UpdateUserProfileImageAppRequest("new profile image");

        // when & then
        mvc.perform(put("/v1/users/profile-image")
                        .contentType(MediaType.APPLICATION_JSON)
                        .requestAttr("userId", userId)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.code").value("S0000"))
                .andExpect(jsonPath("$.message").value("success"));
        then(userServiceV1).should().updateUserProfileImage(userId, request);
    }

    @DisplayName("주량과 금주 목표를 업데이트하면 성공 코드를 응답한다.")
    @Test
    void shouldReturnSuccessWhenUpdatingAlcoholLimitAndGoal() throws Exception {
        // given
        long userId = 1L;
        UpdateAlcoholLimitAndGoalAppRequest request =
                new UpdateAlcoholLimitAndGoalAppRequest(2.0f, 2, AlcoholType.SOJU);

        // when & then
        mvc.perform(put("/v1/users/alcohol-limit-and-goal")
                        .contentType(MediaType.APPLICATION_JSON)
                        .requestAttr("userId", userId)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.code").value("S0000"))
                .andExpect(jsonPath("$.message").value("success"));
        then(userServiceV1).should().updateAlcoholLimitAndGoal(userId, request);
    }

    @DisplayName("닉네임을 업데이트하면 성공 코드를 응답한다.")
    @Test
    void shouldReturnSuccessWhenUpdatingNickname() throws Exception {
        // given
        long userId = 1L;
        UpdateNicknameAppRequest request = new UpdateNicknameAppRequest("newnickname");

        // when & then
        mvc.perform(put("/v1/users/nickname")
                        .contentType(MediaType.APPLICATION_JSON)
                        .requestAttr("userId", userId)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.code").value("S0000"))
                .andExpect(jsonPath("$.message").value("success"));
        then(userServiceV1).should().updateNickname(userId, request);
    }

    @DisplayName("잘못된 형식의 닉네임으로 업데이트를 시도하면 하면 400 응답 코드로 응답한다.")
    @ParameterizedTest
    @ValueSource(strings = {"", "new nickname", "new-nickname"})
    void shouldReturn400WhenUpdatingNicknameWithInvalidFormat(String input) throws Exception {
        // given
        long userId = 1L;
        UpdateNicknameAppRequest request = new UpdateNicknameAppRequest(input);

        // when & then
        mvc.perform(put("/v1/users/nickname")
                        .contentType(MediaType.APPLICATION_JSON)
                        .requestAttr("userId", userId)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.code").value("E9996"))
                .andExpect(jsonPath("$.message").value("[newNickname] 닉네임은 한글, 영어 대소문자, 숫자만 가능합니다."));
        then(userServiceV1).shouldHaveNoInteractions();
    }

    @DisplayName("설명 방식을 변경하면 성공 코드를 응답한다.")
    @Test
    void shouldReturnSuccessWhenUpdatingDescriptionStyle() throws Exception {
        // given
        long userId = 1L;
        UpdateDescriptionStyleAppRequest request = new UpdateDescriptionStyleAppRequest(DescriptionStyle.MALA);

        // when & then
        mvc.perform(put("/v1/users/description-style")
                        .contentType(MediaType.APPLICATION_JSON)
                        .requestAttr("userId", userId)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.code").value("S0000"))
                .andExpect(jsonPath("$.message").value("success"));
        then(userServiceV1).should().updateDescriptionStyle(userId, request);
    }
}
