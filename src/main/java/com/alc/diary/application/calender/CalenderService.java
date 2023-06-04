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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
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
    @CacheEvict(value = "monthlyReport", key = "#userId + '_' + #request.drinkStartDateTime().year + '-' + #request.drinkStartDateTime().month.value", cacheManager = "cacheManager")
    public void save(SaveCalenderRequest request, Long userId) {
        try {
            System.out.println(request.drinkStartDateTime().getYear());
            User user = userRepository.findById(userId).orElseThrow();

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
        int year = calender.getDrinkStartDateTime().getYear();
        int month = calender.getDrinkStartDateTime().getMonth().getValue();
        if (cacheManager.getCache("monthlyReport") != null) {
            cacheManager.getCache("monthlyReport").evict(userId + "_" + year + "-" + month);
        }
        calenderRepository.deleteCalenderById(calender.getId());
    }

    @Transactional
    @CacheEvict(value = "monthlyReport", key = "#userId + '_' + #request.drinkStartDateTime().year + '-' + #request.drinkStartDateTime().month.value", cacheManager = "cacheManager")
    public void update(Long calenderId, Long userId, UpdateCalenderRequest request) {
        if (!isValidUser(calenderId, userId)) return;
        try {
            User user = userRepository.findById(userId).orElseThrow();
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
        Calender calender = calenderRepository.getCalenderById(calenderId).orElseThrow();
        return (Objects.equals(calender.user.getId(), userId));
    }
}
