package com.example.alcdiary.presentation;

import com.example.alcdiary.application.FindCalenderUseCase;
import com.example.alcdiary.presentation.dto.response.GetCalenderResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class CalenderController {

    private final FindCalenderUseCase findCalenderUseCase;

    @GetMapping(value = "/calender/{calender_id}")
    public GetCalenderResponse find(
            @PathVariable(name = "calender_id") Long calenderId
    ) {
        return findCalenderUseCase.execute(calenderId).toResponse();
    }
}
