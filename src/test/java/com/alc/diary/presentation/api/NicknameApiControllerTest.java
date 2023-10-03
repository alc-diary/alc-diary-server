package com.alc.diary.presentation.api;

import com.alc.diary.application.nickname.NicknameAppService;
import com.alc.diary.application.user.dto.response.GetRandomNicknameAppResponse;
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
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(
        value = NicknameApiController.class,
        excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtAuthenticationFilter.class))
class NicknameApiControllerTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    NicknameAppService nicknameAppService;

    @DisplayName("랜덤 닉네임을 가져오면 성공을 반환한다.")
    @Test
    void shouldReturnSuccessWhenGetRandomNickname() throws Exception {
        // given
        String randomNickname = "random nickname";
        given(nicknameAppService.getRandomNickname())
                .willReturn(new GetRandomNicknameAppResponse(randomNickname));

        // when & then
        ResultActions resultActions = mvc.perform(get("/v1/nicknames/random")
                        .contentType(MediaType.APPLICATION_JSON));

        assertSuccess(resultActions)
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data.nickname").value(randomNickname));

        then(nicknameAppService).should().getRandomNickname();
    }

    private ResultActions assertSuccess(ResultActions perform) throws Exception {
        return perform
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.code").value("S0000"))
                .andExpect(jsonPath("$.message").value("success"));
    }

    private ResultActions assertCreated(ResultActions perform) throws Exception {
        return perform
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(201))
                .andExpect(jsonPath("$.code").value("S0001"))
                .andExpect(jsonPath("$.message").value("created"));
    }
}