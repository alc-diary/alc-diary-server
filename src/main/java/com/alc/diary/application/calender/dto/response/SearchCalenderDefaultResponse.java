package com.alc.diary.application.calender.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class SearchCalenderDefaultResponse implements SearchCalenderResponse {
    List<String> calenderResponse;

    public static SearchCalenderDefaultResponse of(){
        return SearchCalenderDefaultResponse.builder()
                .calenderResponse(List.of())
                .build();
    }
}