package com.alc.diary.presentation.api;

import com.alc.diary.application.calendar.CalendarServiceV1;
import com.alc.diary.application.calendar.dto.request.CreateCalendarRequest;
import com.alc.diary.application.calendar.dto.response.CreateCalendarResponse;
import com.alc.diary.application.calendar.dto.response.GetCalendarByIdResponse;
import com.alc.diary.domain.calendar.Calendar;
import com.alc.diary.domain.calendar.enums.DrinkType;
import com.alc.diary.domain.calendar.enums.DrinkUnitType;
import com.alc.diary.presentation.api.calendar.CalendarApiControllerV1;
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
import org.springframework.test.web.servlet.ResultActions;

import java.time.ZonedDateTime;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(
        value = CalendarApiControllerV1.class,
        excludeFilters = {@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtAuthenticationFilter.class)})
class CalendarApiControllerTest {

    @MockBean
    private CalendarServiceV1 calendarService;

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
                List.of(new CreateCalendarRequest.DrinkCreationDto(DrinkType.BEER, DrinkUnitType.BOTTLE, 1.0f))
        );
    }

    private List<CreateCalendarRequest.PhotoCreationDto> getSamplePhotoCreationDto() {
        return List.of(new CreateCalendarRequest.PhotoCreationDto("url"));
    }

    @Test
    void getCalendarByIdTest() throws Exception {
        // given
        long userId = 1L;
        long calendarId = 1L;
        ZonedDateTime start = ZonedDateTime.now();
        ZonedDateTime end = ZonedDateTime.now();
        Calendar calendar = Calendar.create(calendarId, "title", 1.5f, start, end);
        GetCalendarByIdResponse response = new GetCalendarByIdResponse(calendarId, userId, "title", start.toString(), end.toString(), List.of(), List.of());
        when(calendarService.getCalendarById(userId, calendarId))
                .thenReturn(response);

        // when & then
        ResultActions perform = mvc.perform(get("/v1/calendars/" + calendarId)
                .contentType(MediaType.APPLICATION_JSON)
                .requestAttr("userId", userId));
        assertSuccess(perform)
                .andExpect(jsonPath("$.data.calendarId").value(1L))
                .andExpect(jsonPath("$.data.ownerId").value(1L))
                .andExpect(jsonPath("$.data.title").value("title"))
                .andExpect(jsonPath("$.data.drinkStartTime").value(start.toString()))
                .andExpect(jsonPath("$.data.drinkEndTime").value(end.toString()))
                .andExpect(jsonPath("$.data.userCalendars", empty()))
                .andExpect(jsonPath("$.data.photos", empty()));

        then(calendarService).should().getCalendarById(userId, calendarId);
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
