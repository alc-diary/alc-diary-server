package com.example.alcdiary.presentation.api;

import com.example.alcdiary.application.CalenderUseCase;
import com.example.alcdiary.application.command.CreateCalenderCommand;
import com.example.alcdiary.presentation.dto.request.CreateCalenderRequest;
import com.example.alcdiary.presentation.dto.response.GetCalenderResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class CalenderController {

    private final CalenderUseCase calenderUseCase;

    @GetMapping(value = "/calender/{calender_id}")
    public GetCalenderResponse find(
            @PathVariable(name = "calender_id") Long calenderId
    ) {
        return calenderUseCase.find(calenderId).toResponse();
    }


    @PostMapping(value = "/calender/{user_id}")
    public void create(
            @RequestBody CreateCalenderRequest request
    ) {
        calenderUseCase.create(
                CreateCalenderCommand.builder()
                        .title(request.getTitle())
                        .contents(request.getContents())
                        .drinkType(request.getDrinkType())
                        .hangOver(request.getHangOver()).build()
        );
    }
}
