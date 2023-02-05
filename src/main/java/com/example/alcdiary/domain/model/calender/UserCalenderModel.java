package com.example.alcdiary.domain.model.calender;

import com.example.alcdiary.infrastructure.entity.Calender;
import lombok.Data;

import java.util.List;

@Data
public class UserCalenderModel {
    private Calender calender;
    private List<String> friends;

    public UserCalenderModel(Calender calender, List<String> userCalenderIds) {
        this.calender = calender;
        this.friends = userCalenderIds;
    }
}
