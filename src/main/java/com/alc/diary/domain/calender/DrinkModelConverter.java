package com.alc.diary.domain.calender;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.persistence.AttributeConverter;
import java.util.Arrays;
import java.util.List;

public class DrinkModelConverter implements AttributeConverter<List<DrinkModel>, String> {
    private final ObjectMapper objectMapper = new ObjectMapper();


    @Override
    public String convertToDatabaseColumn(List<DrinkModel> drinksModel) {
        try {
            return objectMapper.writeValueAsString(drinksModel);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public List<DrinkModel> convertToEntityAttribute(String dbData) {
        try {
            return Arrays.stream(objectMapper.readValue(dbData, DrinkModel[].class)).toList();
        } catch (Exception e) {
            return null;
        }
    }
}
