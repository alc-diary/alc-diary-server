package com.alc.diary.application.nickname.dto.request;

import org.jetbrains.annotations.NotNull;

import javax.validation.constraints.Size;

public record AddNicknameBannedWordRequest(

        @NotNull @Size(max = 16) String bannedWord
) {
}
