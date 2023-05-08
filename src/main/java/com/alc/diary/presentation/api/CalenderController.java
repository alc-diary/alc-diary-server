package com.alc.diary.presentation.api;

import com.alc.diary.application.calender.CalenderService;
import com.alc.diary.application.calender.dto.request.SaveCalenderRequest;
import com.alc.diary.application.calender.dto.request.SearchCalenderRequest;
import com.alc.diary.application.calender.dto.request.UpdateCalenderRequest;
import com.alc.diary.application.calender.dto.response.FindCalenderDetailResponse;
import com.alc.diary.application.calender.dto.response.SearchCalenderResponse;
import com.alc.diary.domain.calender.enums.QueryType;
import com.alc.diary.presentation.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("v1/calender")
public class CalenderController {

    private final CalenderService calenderService;

    @GetMapping(value = "/{calenderId}")
    public ApiResponse<FindCalenderDetailResponse> find(@PathVariable Long calenderId) {
        return ApiResponse.getSuccess(calenderService.find(calenderId));
    }

    @GetMapping(value = "search")
    public ApiResponse<SearchCalenderResponse> search(
            @RequestParam(value = "query", required = false) String query,
            @RequestParam(value = "date", required = false) String date,
            @RequestAttribute Long userId
    ) {
        return ApiResponse.getSuccess(calenderService.search(new SearchCalenderRequest(userId, QueryType.valueOf(query), date)));
    }

    @PostMapping(value = "")
    public ApiResponse<Void> save(@RequestBody @Validated SaveCalenderRequest request,
                                  @RequestAttribute Long userId) {
        calenderService.save(request, userId);
        return ApiResponse.getSuccess();
    }

    @PutMapping(value = "/{calenderId}")
    public ApiResponse<Void> update(@PathVariable Long calenderId,
                                    @RequestAttribute Long userId,
                                    @RequestBody UpdateCalenderRequest request) {
        calenderService.update(calenderId, userId, request);
        return ApiResponse.getSuccess();
    }

    @DeleteMapping(value = "/{calenderId}")
    public ApiResponse<Void> delete(@PathVariable Long calenderId,
                                    @RequestAttribute Long userId) {
        calenderService.delete(calenderId, userId);
        return ApiResponse.getSuccess();
    }
}
