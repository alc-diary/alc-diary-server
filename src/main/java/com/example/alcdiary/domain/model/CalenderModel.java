package com.example.alcdiary.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
public class CalenderModel {

    private Long id;
    private String title;
}
