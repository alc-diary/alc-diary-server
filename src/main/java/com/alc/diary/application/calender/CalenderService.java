package com.alc.diary.application.calender;

import com.alc.diary.application.calender.dto.request.SaveCalenderRequest;
import com.alc.diary.application.calender.dto.request.SearchCalenderRequest;
import com.alc.diary.application.calender.dto.request.UpdateCalenderRequest;
import com.alc.diary.application.calender.dto.response.*;
import com.alc.diary.domain.calender.Calender;
import com.alc.diary.domain.calender.error.CalenderError;
import com.alc.diary.domain.calender.model.CalenderImage;
import com.alc.diary.domain.calender.repository.CalenderRepository;
import com.alc.diary.domain.calender.repository.CustomCalenderRepository;
import com.alc.diary.domain.exception.CalenderException;
import com.alc.diary.domain.user.User;
import com.alc.diary.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CalenderService {

    private final UserRepository userRepository;
    private final CalenderRepository calenderRepository;
    private final CustomCalenderRepository customCalenderRepository;
    private final CacheManager cacheManager;

    @Transactional(readOnly = true)
    public FindCalenderDetailResponse find(Long calenderId) {
        try {
            Calender calender = calenderRepository.getCalenderById(calenderId).orElseThrow();
            return FindCalenderDetailResponse.of(
                    calender.getTitle(), calender.getContents(),
                    calender.getDrinkStartDateTime(), calender.getDrinkEndDateTime(),
                    calender.getDrinkModels(), (calender.getImage() == null) ? null : calender.getImage().getImages(),
                    calender.getDrinkCondition()
            );
        } catch (Throwable e) {
            return null;
        }
    }

    // TODO: 앱 출시되면 삭제 필요(v1)
    @Transactional(readOnly = true)
    public SearchCalenderResponse searchV1(SearchCalenderRequest request) {
        try {
            List<Calender> searchCalenders = customCalenderRepository.search(request.userId(), request.query(), LocalDate.parse(request.date()));
            if (searchCalenders.isEmpty()) return SearchCalenderDefaultResponse.of();

            return switch (request.query()) {
                case MONTH -> SearchCalenderMonthResponse.of(searchCalenders);
                case DAY -> SearchCalenderDayV1Response.of(searchCalenders);
            };
        } catch (Throwable e) {
            e.printStackTrace();
            throw new CalenderException(CalenderError.INVALID_PARAMETER_INCLUDE);
        }
    }

    @Transactional(readOnly = true)
    public SearchCalenderResponse search(SearchCalenderRequest request) {
        try {
            List<Calender> searchCalenders = customCalenderRepository.search(request.userId(), request.query(), LocalDate.parse(request.date()));
            if (searchCalenders.isEmpty()) return SearchCalenderDefaultResponse.of();

            return switch (request.query()) {
                case MONTH -> SearchCalenderMonthResponse.of(searchCalenders);
                case DAY -> SearchCalenderDayResponse.of(searchCalenders);
            };
        } catch (Throwable e) {
            e.printStackTrace();
            throw new CalenderException(CalenderError.INVALID_PARAMETER_INCLUDE);
        }
    }

    @Transactional
    @Caching(evict = {
            @CacheEvict(value = "monthlyReport", key = "#userId + '_' + #request.drinkStartDateTime().year + '-' + #request.drinkStartDateTime().monthValue", cacheManager = "cacheManager"),
            @CacheEvict(value = "monthlyReport", key = "#userId + '_' + #request.drinkStartDateTime().plusMonths(1).year + '-' + #request.drinkStartDateTime().plusMonths(1).monthValue", cacheManager = "cacheManager")
    })
    public void save(SaveCalenderRequest request, Long userId) {
        try {
            User user = userRepository.findActiveUserById(userId).orElseThrow();

            calenderRepository.save(
                    Calender.builder()
                            .title(request.title())
                            .contents(request.contents())
                            .drinkStartDateTime(request.drinkStartDateTime())
                            .drinkEndDateTime(request.drinkEndDateTime())
                            .drinkModels(request.drinkModels())
                            .image(new CalenderImage(request.images()))
                            .drinkCondition(request.drinkCondition())
                            .user(user)
                            .build()
            );
        } catch (Exception e) {
            throw new CalenderException(CalenderError.NO_ENTITY_FOUND);
        }
    }

    @Transactional
    public void delete(Long calenderId, Long userId) {
        if (!isValidUser(calenderId, userId)) return;
        Calender calender = calenderRepository.findById(calenderId).orElseThrow(() -> new CalenderException(CalenderError.NO_ENTITY_FOUND));
        evictMonthlyReportCacheForCurrentAndNextMonth(userId, calender);
        calenderRepository.deleteCalenderById(calender.getId());
    }

    private void evictMonthlyReportCacheForCurrentAndNextMonth(Long userId, Calender calender) {
        LocalDateTime drinkStartDateTime = calender.getDrinkStartDateTime();
        LocalDateTime drinkStartDateTimePlusOneMonth = drinkStartDateTime.plusMonths(1);
        if (cacheManager.getCache("monthlyReport") != null) {
            cacheManager.getCache("monthlyReport").evict(userId + "_" + drinkStartDateTime.getYear() + "-" + drinkStartDateTime.getMonthValue());
            cacheManager.getCache("monthlyReport").evict(userId + "_" + drinkStartDateTimePlusOneMonth.getYear() + "-" + drinkStartDateTimePlusOneMonth.getMonthValue());
        }
    }

    @Transactional
    @Caching(evict = {
            @CacheEvict(value = "monthlyReport", key = "#userId + '_' + #request.drinkStartDateTime().year + '-' + #request.drinkStartDateTime().monthValue", cacheManager = "cacheManager"),
            @CacheEvict(value = "monthlyReport", key = "#userId + '_' + #request.drinkStartDateTime().plusMonths(1).year + '-' + #request.drinkStartDateTime().plusMonths(1).monthValue", cacheManager = "cacheManager")
    })
    public void update(Long calenderId, Long userId, UpdateCalenderRequest request) {
        if (!isValidUser(calenderId, userId)) return;
        try {
            User user = userRepository.findActiveUserById(userId).orElseThrow();
            Calender calender = calenderRepository.getCalenderById(calenderId).orElseThrow();
            calender.update(
                    request.title(), request.contents(),
                    request.drinkStartDateTime(), request.drinkEndDateTime(),
                    request.drinkModels(), new CalenderImage(request.images()),
                    request.drinkCondition(), user
            );
        } catch (Exception e) {
            throw new CalenderException(CalenderError.NO_ENTITY_FOUND);
        }
    }

    private Boolean isValidUser(Long calenderId, Long userId) {
        Calender calender = calenderRepository.getCalenderById(calenderId).orElseThrow(() -> new CalenderException(CalenderError.NO_ENTITY_FOUND));
        return (Objects.equals(calender.user.getId(), userId));
    }
}
