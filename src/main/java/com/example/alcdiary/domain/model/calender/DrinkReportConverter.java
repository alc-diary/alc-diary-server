package com.example.alcdiary.domain.model.calender;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.persistence.AttributeConverter;

public class DrinkReportConverter implements AttributeConverter<DrinkReportModel, String> {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(DrinkReportModel drinkReportModel) {
        try {
            return objectMapper.writeValueAsString(drinkReportModel);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public DrinkReportModel convertToEntityAttribute(String dbData) {
        try {
            return objectMapper.readValue(dbData, DrinkReportModel.class);
        } catch (Exception e) {
            return null;
        }
    }
}
