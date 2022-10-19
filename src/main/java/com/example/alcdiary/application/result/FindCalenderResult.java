package com.example.alcdiary.application.result;

import com.example.alcdiary.domain.model.CalenderModel;
import com.example.alcdiary.presentation.dto.response.GetCalenderResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FindCalenderResult {

    private String title;

    public FindCalenderResult fromModel(CalenderModel calenderModel) {
        return FindCalenderResult.builder()
                .title(calenderModel.getTitle())
                .build();
    }

    public GetCalenderResponse toResponse() {
        return GetCalenderResponse.builder()
                .title(title)
                .build();
    }
}
