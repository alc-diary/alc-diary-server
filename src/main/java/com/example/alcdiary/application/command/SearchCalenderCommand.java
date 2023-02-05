package com.example.alcdiary.application.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SearchCalenderCommand {
    private Integer month;

    private Integer day;

    private String userId;
}
