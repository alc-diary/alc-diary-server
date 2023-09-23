package com.alc.diary.presentation.api;

import com.alc.diary.application.calendar.CalendarService;
import com.alc.diary.application.calendar.dto.request.CreateCalendarRequest;
import com.alc.diary.application.calendar.dto.response.CreateCalendarResponse;
import com.alc.diary.application.calendar.dto.response.GetCalendarByIdResponse;
import com.alc.diary.domain.calendar.enums.DrinkType;
import com.alc.diary.domain.calendar.enums.DrinkUnit;
import com.alc.diary.presentation.filter.JwtAuthenticationFilter;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.ZonedDateTime;
import java.util.List;

import static org.hamcrest.Matchers.hasItems;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(
        value = CalendarApiController.class,
        excludeFilters = {@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtAuthenticationFilter.class)})
class CalendarApiControllerTest {

    @MockBean
    private CalendarService calendarService;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createCalendarTest() throws Exception {
        // given
        given(calendarService.createCalendarAndGenerateResponse(anyLong(), any()))
                .willReturn(new CreateCalendarResponse(1L, List.of(1L, 2L, 3L)));
        CreateCalendarRequest request = getSampleCreateCalendarRequest();

        // when & then
        mvc.perform(post("/v1/calendars")
                        .contentType(MediaType.APPLICATION_JSON)
                        .requestAttr("userId", 1L)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(201))
                .andExpect(jsonPath("$.code").value("S0001"))
                .andExpect(jsonPath("$.message").value("created"))
                .andExpect(jsonPath("$.data.calendarId").value(1L))
                .andExpect(jsonPath("$.data.userCalendarIds", hasItems(1, 2, 3)))
                .andDo(print());
        then(calendarService).should().createCalendarAndGenerateResponse(anyLong(), any());
    }

    private CreateCalendarRequest getSampleCreateCalendarRequest() {
        CreateCalendarRequest.UserCalendarCreationDto userCalendar = getSampleUserCalendarCreationDto();
        List<CreateCalendarRequest.PhotoCreationDto> photos = getSamplePhotoCreationDto();
        List<Long> taggedUserIds = List.of(2L, 3L);

        return new CreateCalendarRequest(
                "title",
                ZonedDateTime.now(),
                ZonedDateTime.now().plusHours(1),
                photos,
                userCalendar,
                taggedUserIds
        );
    }

    private CreateCalendarRequest.UserCalendarCreationDto getSampleUserCalendarCreationDto() {
        return new CreateCalendarRequest.UserCalendarCreationDto(
                1L,
                "content",
                "condition",
                List.of(new CreateCalendarRequest.DrinkCreationDto(DrinkType.BEER, DrinkUnit.BOTTLE, 1.0f))
        );
    }

    private List<CreateCalendarRequest.PhotoCreationDto> getSamplePhotoCreationDto() {
        return List.of(new CreateCalendarRequest.PhotoCreationDto("url"));
    }

    @Test
    void getCalendarByIdTest() {
        // given
        given(calendarService.getCalendarById(anyLong(), anyLong()));

        // when & then
        then(calendarService).should().getCalendarById(anyLong(), anyLong());
    }
}
