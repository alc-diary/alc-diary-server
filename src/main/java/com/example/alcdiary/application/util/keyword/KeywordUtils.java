package com.example.alcdiary.application.util.keyword;

import com.example.alcdiary.application.util.keyword.dto.DeleteKeywordDto;
import com.example.alcdiary.application.util.keyword.dto.GetRandomKeywordListDto;
import com.example.alcdiary.application.util.keyword.dto.SaveKeywordDto;

public interface KeywordUtils {

    String generateNickname();

    GetRandomKeywordListDto getRandomKeywordList();

    void saveKeyword(SaveKeywordDto dto);

    void deleteKeyword(DeleteKeywordDto dto);
}
