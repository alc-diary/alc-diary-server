package com.alc.diary.domain.nickname;

import com.alc.diary.domain.exception.ErrorModel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum NicknameBannedWordError implements ErrorModel {

    NICKNAME_BANNED_WORD_LENGTH_EXCEEDED("NBW_E0000", ""),
    WORD_NULL("NBW_E0001", "Banned word cannot be null."),
    ;

    private final String code;
    private final String message;
}
