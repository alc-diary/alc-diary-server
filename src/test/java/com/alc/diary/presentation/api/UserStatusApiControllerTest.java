package com.alc.diary.presentation.api;

import com.alc.diary.application.onboarding.dto.response.GetIsOnboardingDoneAppResponse;
import com.alc.diary.application.user.UserStatusAppService;
import com.alc.diary.presentation.filter.JwtAuthenticationFilter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
        value = UserStatusApiController.class,
        excludeFilters = {@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtAuthenticationFilter.class)})
class UserStatusApiControllerTest {

    @MockBean
    private UserStatusAppService userStatusAppService;

    @Autowired
    private MockMvc mvc;

    @DisplayName("온보딩이 완료된 경우 true를 반환한다.")
    @Test
    void whenServiceReturnsTrueForIsOnboardingDone_thenReturnsTrueAndStatus200() throws Exception {
        // given
        given(userStatusAppService.getIsOnboardingDone(anyLong()))
                .willReturn(new GetIsOnboardingDoneAppResponse(true));

        // when & then
        mvc.perform(get("/v1/user-status/is-onboarding-done")
                        .contentType(MediaType.APPLICATION_JSON)
                        .requestAttr("userId", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.code").value("S0000"))
                .andExpect(jsonPath("$.message").value("success"))
                .andExpect(jsonPath("$.data.isOnboardingDone").value(true));
        then(userStatusAppService).should().getIsOnboardingDone(anyLong());
    }

    @DisplayName("온보딩이 완료되지 않은 경우 false를 반환한다.")
    @Test
    void whenServiceReturnsFalseForIsOnboardingDone_thenReturnsFalseAndStatus200() throws Exception {
        // given
        given(userStatusAppService.getIsOnboardingDone(anyLong()))
                .willReturn(new GetIsOnboardingDoneAppResponse(false));

        // when & then
        mvc.perform(get("/v1/user-status/is-onboarding-done")
                        .contentType(MediaType.APPLICATION_JSON)
                        .requestAttr("userId", 2L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.code").value("S0000"))
                .andExpect(jsonPath("$.message").value("success"))
                .andExpect(jsonPath("$.data.isOnboardingDone").value(false));
        then(userStatusAppService).should().getIsOnboardingDone(anyLong());
    }
}
