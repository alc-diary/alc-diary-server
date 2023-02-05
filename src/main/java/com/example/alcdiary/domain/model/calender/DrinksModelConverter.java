package com.example.alcdiary.domain.model.calender;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.persistence.AttributeConverter;
import java.util.Arrays;
import java.util.List;

public class DrinksModelConverter implements AttributeConverter<List<DrinksModel>, String> {
    private final ObjectMapper objectMapper = new ObjectMapper();


    @Override
    public String convertToDatabaseColumn(List<DrinksModel> drinksModel) {
        try {
            return objectMapper.writeValueAsString(drinksModel);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public List<DrinksModel> convertToEntityAttribute(String dbData) {
        try {
            return Arrays.stream(objectMapper.readValue(dbData, DrinksModel[].class)).toList();
        } catch (Exception e) {
            return null;
        }
    }
}