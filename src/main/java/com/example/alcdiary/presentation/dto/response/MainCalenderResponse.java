package com.example.alcdiary.presentation.dto.response;

import com.example.alcdiary.domain.model.user.EUserTheme;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MainCalenderResponse {
    String nickname;
    EUserTheme userTheme;

    String comment;

    int decideOverDays;
}
