package com.example.alcdiary.application.impl;

import com.example.alcdiary.application.GetRandomKeywordListUseCase;
import com.example.alcdiary.application.result.GetRandomKeywordListResult;
import com.example.alcdiary.application.util.keyword.KeywordUtils;
import com.example.alcdiary.application.util.keyword.dto.GetRandomKeywordListDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
class GetRandomKeywordListUseCaseImpl implements GetRandomKeywordListUseCase {

    private final KeywordUtils keywordUtils;

    @Override
    public GetRandomKeywordListResult execute() {
        GetRandomKeywordListDto randomKeywordList = keywordUtils.getRandomKeywordList();
        return new GetRandomKeywordListResult(
                randomKeywordList.getFirstKeywordList(),
                randomKeywordList.getSecondKeywordList()
        );
    }
}
