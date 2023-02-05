package com.example.alcdiary.domain.model.calender;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

@Data
public class CalenderAndUserDataModel {
    private Long calenderId;
    private String title;
    private List<DrinksModel> drinkType;
    private List<String> userProfileImageUrls;
    private String userId;
    private Time drinkStartTime;

    private Time drinkEndTime;


    @QueryProjection
    public CalenderAndUserDataModel(Long calenderId, String title, List<DrinksModel> drinkType,
                                    String userId, Time drinkStartTime, Time drinkEndTime) {
        this.calenderId = calenderId;
        this.title = title;
        this.drinkType = drinkType;
        this.userProfileImageUrls = new ArrayList<>();
        this.userId = userId;
        this.drinkStartTime = drinkStartTime;
        this.drinkEndTime = drinkEndTime;
    }
}
