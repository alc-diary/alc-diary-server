package com.example.alcdiary.application;

import com.example.alcdiary.application.command.ReIssueAccessTokenCommand;
import com.example.alcdiary.application.result.ReIssueAccessTokenResult;

public interface ReIssueAccessTokenUseCase {

    ReIssueAccessTokenResult execute(ReIssueAccessTokenCommand command);
}
