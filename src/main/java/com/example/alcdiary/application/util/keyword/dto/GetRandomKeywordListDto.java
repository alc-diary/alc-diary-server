package com.example.alcdiary.application.util.keyword.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class GetRandomKeywordListDto {

    private List<String> firstKeywordList;
    private List<String> secondKeywordList;
}
