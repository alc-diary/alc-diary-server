package com.alc.diary.domain.user.repository;

import com.alc.diary.domain.calendar.UserCalendar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserCalendarRepository extends JpaRepository<UserCalendar, Long> {

    @Query(value = "WITH ranked_calendars AS (" +
                   "    SELECT user_id, created_at, " +
                   "           ROW_NUMBER() OVER (PARTITION BY user_id ORDER BY created_at) AS rn " +
                   "    FROM user_calendars " +
                   "    WHERE user_id IN :userIds" +
                   ") " +
                   "SELECT user_id, created_at " +
                   "FROM ranked_calendars " +
                   "WHERE rn <= 2",
            nativeQuery = true)
    List<Object[]> findTop2RecentCalendarsByUserIds(List<Long> userIds);
}
