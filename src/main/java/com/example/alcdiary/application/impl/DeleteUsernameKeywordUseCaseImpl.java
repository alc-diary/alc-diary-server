package com.example.alcdiary.application.impl;

import com.example.alcdiary.application.DeleteUsernameKeywordUseCase;
import com.example.alcdiary.application.command.DeleteUsernameKeywordCommand;
import com.example.alcdiary.application.util.keyword.KeywordUtils;
import com.example.alcdiary.application.util.keyword.dto.DeleteKeywordDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
class DeleteUsernameKeywordUseCaseImpl implements DeleteUsernameKeywordUseCase {

    private final KeywordUtils keywordUtils;

    @Override
    public void execute(DeleteUsernameKeywordCommand command) {
        DeleteKeywordDto dto = new DeleteKeywordDto(command.getKeyword(), command.getLocation());
        keywordUtils.deleteKeyword(dto);
    }
}
