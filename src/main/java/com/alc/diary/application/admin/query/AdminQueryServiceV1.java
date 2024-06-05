package com.alc.diary.application.admin.query;

import com.alc.diary.domain.user.repository.UserCalendarRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class AdminQueryServiceV1 {

    private final UserCalendarRepository userCalendarRepository;

    public List<TestQueryResponse> getQuery(List<Long> userIds) {
        List<Object[]> rawResults = userCalendarRepository.findTop2RecentCalendarsByUserIds(userIds);

        return rawResults.stream()
                .map(result -> new TestQueryResponse((Long) result[0], (LocalDateTime) result[1]))
                .toList();
    }
}
