package com.example.alcdiary.application.command;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class SaveUsernameKeywordCommand {

    private String keyword;
    private String location;
}
