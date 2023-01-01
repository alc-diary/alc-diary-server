package com.example.alcdiary.application.result;

import com.example.alcdiary.domain.model.calender.CalenderModel;
import com.example.alcdiary.domain.model.calender.DrinksModel;
import com.example.alcdiary.presentation.dto.response.FindCalenderResponse;
import lombok.*;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class FindCalenderResult {

    private Long calenderId;

    private String title;

    private String drinkTime;


    private DrinksModel[] drinks;
    private String hangOver;
    private String contents;

    private String[] imageUrl;

    public FindCalenderResult fromModel(CalenderModel calenderModel) {
        SimpleDateFormat dateformat = new SimpleDateFormat("a HH:mm");
        String drinkTimes = " " + dateformat.format(calenderModel.getDrinkStartTime()) +
                " " + dateformat.format(calenderModel.getDrinkEndTime());
        return FindCalenderResult.builder()
                .calenderId(calenderModel.getId())
                .title(calenderModel.getTitle())
                .drinkTime(calenderModel.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy.MM.dd")) + drinkTimes)
                .drinks(calenderModel.getDrinks())
                .hangOver(calenderModel.getHangOver())
                .contents(calenderModel.getContents())
                .imageUrl(calenderModel.getImageUrl())
                .build();
    }

    public FindCalenderResponse toResponse() {
        return FindCalenderResponse.builder()
                .calenderId(calenderId)
                .title(title)
                .drinkTime(drinkTime)
                .drinks(drinks)
                .totalDrinkCount(Arrays.stream(drinks)
                        .mapToInt(DrinksModel::getQuantity).sum())
                .hangOver(hangOver)
                .contents(contents)
                .imageUrl(imageUrl)
                .build();
    }
}
