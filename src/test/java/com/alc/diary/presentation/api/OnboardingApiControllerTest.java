package com.alc.diary.presentation.api;

import com.alc.diary.application.nickname.NicknameAppService;
import com.alc.diary.application.onboarding.OnboardingAppService;
import com.alc.diary.application.onboarding.dto.response.GetIsOnboardingDoneAppResponse;
import com.alc.diary.application.user.UserStatusAppService;
import com.alc.diary.application.user.dto.response.CheckNicknameAvailableAppResponse;
import com.alc.diary.application.user.dto.response.GetRandomNicknameAppResponse;
import com.alc.diary.presentation.filter.JwtAuthenticationFilter;
import com.alc.diary.testuils.RandomStringGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.stream.Stream;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(
        value = OnboardingApiController.class,
        excludeFilters = {@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtAuthenticationFilter.class)})
class OnboardingApiControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private OnboardingAppService onboardingAppService;

    @MockBean
    private NicknameAppService nicknameAppService;

    @MockBean
    private UserStatusAppService userStatusAppService;

    @DisplayName("유저 ID로 온보딩 종료 여부를 조회한다.")
    @ValueSource(booleans = {true, false})
    @ParameterizedTest
    void shouldReturnIsOnboardingDoneWhenUserIdGiven(boolean input) throws Exception {
        // given
        long userId = 1L;
        given(userStatusAppService.getIsOnboardingDone(userId))
                .willReturn(new GetIsOnboardingDoneAppResponse(input));

        // when & then
        mvc.perform(get("/v1/onboarding/is-onboarding-done")
                        .contentType(MediaType.APPLICATION_JSON)
                        .requestAttr("userId", userId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.code").value("S0000"))
                .andExpect(jsonPath("$.message").value("success"))
                .andExpect(jsonPath("$.data.isOnboardingDone").value(input));
        then(userStatusAppService).should().getIsOnboardingDone(userId);
    }

    @DisplayName("닉네임 형식에 맞는 문자열이 주어질 때 사용 가능 여부를 응답한다.")
    @MethodSource("formattedRandomStringProvider")
    @ParameterizedTest
    void shouldReturnNicknameAvailableWhenNicknameGiven(String input) throws Exception {
        // given
        given(onboardingAppService.checkNicknameAvailable(input))
                .willReturn(new CheckNicknameAvailableAppResponse(true));

        // when & then
        mvc.perform(get("/v1/onboarding/check-nickname-available")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("nickname", input))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.code").value("S0000"))
                .andExpect(jsonPath("$.message").value("success"))
                .andExpect(jsonPath("$.data.isAvailable").value(true));
        then(userStatusAppService).shouldHaveNoInteractions();
    }

    private static Stream<String> formattedRandomStringProvider() {
        return Stream.generate(() -> RandomStringGenerator.generateRandomLengthString(16)).limit(10);
    }

    @DisplayName("형식에 맞지않는 문자열이 주어질 때 400 응답을 보낸다.")
    @Test
    void shouldReturn400WhenUnformattedStringGiven() throws Exception {
        // given
        String nickname = "";

        // when & then
        mvc.perform(get("/v1/onboarding/check-nickname-available")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("nickname", nickname))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.code").value("US_E0011"))
                .andExpect(jsonPath("$.message").value("닉네임은 한글, 영어 대소문자, 숫자로만 검색할 수 있습니다."));
    }

    @DisplayName("랜덤 닉네임을 응답한다.")
    @Test
    void shouldReturnRandomNickname() throws Exception {
        // given
        String randomNickname = "randomNickname";
        given(nicknameAppService.getRandomNickname())
                .willReturn(new GetRandomNicknameAppResponse(randomNickname));

        // when & then
        mvc.perform(get("/v1/onboarding/random-nickname")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.code").value("S0000"))
                .andExpect(jsonPath("$.message").value("success"))
                .andExpect(jsonPath("$.data.nickname").value(randomNickname));
    }
}
