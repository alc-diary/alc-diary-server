package com.alc.diary.presentation.api;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// @DisplayName("캘린더 컨트롤러")
// @WebMvcTest(CalendarApiController.class)
// @SpringBootTest
class CalendarApiControllerTest {

    private final MockMvc mvc;

    @MockBean
    private CalendarApiController calendarApiController;

    public CalendarApiControllerTest(@Autowired MockMvc mvc) {
        this.mvc = mvc;
    }

    @DisplayName("aa")
    @Test
    void name() throws Exception {
        // given
        mvc.perform(get("/"))
                .andExpect(status().isOk());
    }
}
