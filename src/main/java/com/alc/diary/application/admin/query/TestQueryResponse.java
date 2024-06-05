package com.alc.diary.application.admin.query;

import java.time.LocalDateTime;

public record TestQueryResponse(

        long userId,
        LocalDateTime createdAt
) {
}
