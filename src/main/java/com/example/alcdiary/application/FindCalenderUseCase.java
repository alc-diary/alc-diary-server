package com.example.alcdiary.application;

import com.example.alcdiary.application.result.FindCalenderResult;

public interface FindCalenderUseCase {

    FindCalenderResult execute(Long calenderId);
}
