package com.alc.diary.presentation.api.admin;

import com.alc.diary.application.admin.query.AdminQueryServiceV1;
import com.alc.diary.application.admin.query.TestQueryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/admin/v1/queries")
@RestController
public class AdminQueryControllerV1 {

    private final AdminQueryServiceV1 adminQueryService;

    @GetMapping("/test")
    public ResponseEntity<List<TestQueryResponse>> getQuery(@RequestParam List<Long> userIds) {
        return ResponseEntity.ok(adminQueryService.getQuery(userIds));
    }
}
