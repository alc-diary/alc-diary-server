package com.example.alcdiary.application.command;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DeleteUsernameKeywordCommand {

    private String keyword;
    private String location;
}
