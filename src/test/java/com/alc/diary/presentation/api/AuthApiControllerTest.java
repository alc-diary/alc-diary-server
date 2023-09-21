package com.alc.diary.presentation.api;

import com.alc.diary.application.auth.dto.request.SocialLoginAppRequest;
import com.alc.diary.application.auth.dto.response.SocialLoginAppResponse;
import com.alc.diary.application.auth.service.RefreshTokenAppService;
import com.alc.diary.application.auth.service.SocialLoginAppService;
import com.alc.diary.domain.user.enums.SocialType;
import com.alc.diary.presentation.filter.JwtAuthenticationFilter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(
        value = AuthApiController.class,
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtAuthenticationFilter.class)})
class AuthApiControllerTest {

    @MockBean
    private SocialLoginAppService socialLoginAppService;

    @MockBean
    private RefreshTokenAppService refreshTokenAppService;

    @Autowired
    private MockMvc mvc;

    @Test
    void socialLogin() throws Exception {
        // given
        SocialType socialType = SocialType.KAKAO;
        String socialAccessToken = "test-token";
        String userAgent = "ios";
        SocialLoginAppRequest request = new SocialLoginAppRequest(socialType, socialAccessToken);

        String accessToken = "test_access-token";
        String refreshToken = "test_refresh-token";

        given(socialLoginAppService.login(request, userAgent))
                .willReturn(new SocialLoginAppResponse(accessToken, refreshToken));

        // when & then
        mvc.perform(post("/v1/auth/social-login")
                        .header("Authorization", "Bearer " + socialAccessToken)
                        .header("User-Agent", userAgent)
                        .param("socialType", "KAKAO"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.code").value("S0000"))
                .andExpect(jsonPath("$.message").value("success"))
                .andExpect(jsonPath("$.data.accessToken").value(accessToken))
                .andExpect(jsonPath("$.data.refreshToken").value(refreshToken));
    }


}
