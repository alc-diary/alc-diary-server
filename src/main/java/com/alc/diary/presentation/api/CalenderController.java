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
import springfox.documentation.annotations.ApiIgnore;

@Deprecated
@RestController
@RequiredArgsConstructor
public class CalenderController {

    private final CalenderService calenderService;

    /**
     * 캘린더 상세 조회
     *
     * @param calenderId 캘린더 ID
     * @return 캘린더 상세
     */
    @GetMapping(value = "v1/calender/{calenderId}")
    public ApiResponse<FindCalenderDetailResponse> find(@PathVariable Long calenderId) {
        return ApiResponse.getSuccess(calenderService.find(calenderId));
    }

    /**
     * 캘린더 검색
     *
     * @param query  검색어
     * @param date   날짜
     * @param userId 사용자 ID 
     * @return 캘린더 검색 결과
     */
    @GetMapping(value = "v1/calender/search")
    public ApiResponse<SearchCalenderResponse> search(
            @RequestParam(value = "query", required = false) String query,
            @RequestParam(value = "date", required = false) String date,
            @ApiIgnore @RequestAttribute Long userId
    ) {
        return ApiResponse.getSuccess(calenderService.searchV1(new SearchCalenderRequest(userId, QueryType.valueOf(query), date)));
    }

    /**
     * 캘린더 검색
     *
     * @param query  검색어
     * @param date   날짜
     * @param userId 사용자 ID
     * @return 캘린더 검색 결과
     */
    @GetMapping(value = "v2/calender/search")
    public ApiResponse<SearchCalenderResponse> searchV2(
            @RequestParam(value = "query", required = false) String query,
            @RequestParam(value = "date", required = false) String date,
            @ApiIgnore @RequestAttribute Long userId
    ) {
        return ApiResponse.getSuccess(calenderService.search(new SearchCalenderRequest(userId, QueryType.valueOf(query), date)));
    }

    /**
     * 캘린더 저장
     *
     * @param request 캘린더 저장 요청
     * @param userId  사용자 ID
     * @return 성공
     */
    @PostMapping(value = "v1/calender")
    public ApiResponse<Void> save(@RequestBody @Validated SaveCalenderRequest request,
                                  @ApiIgnore @RequestAttribute Long userId) {
        calenderService.save(request, userId);
        return ApiResponse.getSuccess();
    }

    /**
     * 캘린더 수정
     *
     * @param calenderId 캘린더 ID
     * @param userId 사용자 ID
     * @param request 캘린더 수정 요청
     * @return 성공
     */
    @PutMapping(value = "v1/calender/{calenderId}")
    public ApiResponse<Void> update(@PathVariable Long calenderId,
                                    @ApiIgnore @RequestAttribute Long userId,
                                    @RequestBody UpdateCalenderRequest request) {
        calenderService.update(calenderId, userId, request);
        return ApiResponse.getSuccess();
    }

    /**
     *  캘린더 삭제
     *
     * @param calenderId 캘린더 ID
     * @param userId 사용자 ID
     * @return 성공
     */
    @DeleteMapping(value = "v1/calender/{calenderId}")
    public ApiResponse<Void> delete(@PathVariable Long calenderId,
                                    @ApiIgnore @RequestAttribute Long userId) {
        calenderService.delete(calenderId, userId);
        return ApiResponse.getSuccess();
    }
}
