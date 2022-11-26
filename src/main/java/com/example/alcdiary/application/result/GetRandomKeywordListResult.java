package com.example.alcdiary.application.result;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class GetRandomKeywordListResult {

    private List<String> firstKeywordList;
    private List<String> secondKeywordList;
}
