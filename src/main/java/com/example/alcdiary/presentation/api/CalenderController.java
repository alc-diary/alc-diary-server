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

    @GetMapping(value = "/calender/{calenderId}")
    public GetCalenderResponse find(
            @PathVariable(name = "calenderId") Long calenderId
    ) {
        return calenderUseCase.find(calenderId).toResponse();
    }


    @PostMapping(value = "/calender/{userId}")
    public void create(
            @RequestBody CreateCalenderRequest request,
            @PathVariable String userId) {
        calenderUseCase.create(
                CreateCalenderCommand.builder()
                        .userId(userId)
                        .title(request.getTitle())
                        .friends(request.getFriends())
                        .drinks(request.getDrinks())
                        .hangOver(request.getHangOver())
                        .drinkStartTime(request.getDrinkStartTime())
                        .drinkEndTime(request.getDrinkEndTime())
                        .imageUrl(request.getImageUrl())
                        .contents(request.getContents())
                        .build()
        );
    }
}
