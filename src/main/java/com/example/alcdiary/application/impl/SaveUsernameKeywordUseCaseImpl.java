package com.example.alcdiary.application.impl;

import com.example.alcdiary.application.SaveUsernameKeywordUseCase;
import com.example.alcdiary.application.command.SaveUsernameKeywordCommand;
import com.example.alcdiary.application.util.keyword.KeywordUtils;
import com.example.alcdiary.application.util.keyword.dto.SaveKeywordDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
class SaveUsernameKeywordUseCaseImpl implements SaveUsernameKeywordUseCase {

    private final KeywordUtils keywordUtils;

    @Override
    public void execute(SaveUsernameKeywordCommand command) {
        SaveKeywordDto dto = new SaveKeywordDto(command.getKeyword(), command.getLocation());
        keywordUtils.saveKeyword(dto);
    }
}
