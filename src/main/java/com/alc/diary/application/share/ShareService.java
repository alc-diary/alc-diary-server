package com.alc.diary.application.share;

import com.alc.diary.application.calender.CalenderService;
import com.alc.diary.application.calender.dto.response.FindCalenderDetailResponse;
import com.alc.diary.application.share.dto.ShareCalenderDetailResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Base64;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ShareService {

    private final CalenderService calenderService;

    public String createShareLink(String userId, String calenderId) {
        try {
            String data = calenderId + "/" + userId;
            return Base64.getUrlEncoder().encodeToString(data.getBytes());
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    public ShareCalenderDetailResponse getLinkCalender(String encodedData) {
        try {
            String decoded = new String(Base64.getUrlDecoder().decode(encodedData));
            List<String> result = Arrays.stream(decoded.split("/")).toList();

            long writerUserId = Long.parseLong(result.get(1)); // 공유한 사람 userId
            long calenderId = Long.parseLong(result.get(0));

            FindCalenderDetailResponse response = calenderService.find(calenderId);
            return ShareCalenderDetailResponse.of(response, writerUserId, calenderId);
        } catch (Exception e) {
            log.error("일치하는 캘린더 데이터가 없습니다.", e);
            return null;
        }
    }
}
