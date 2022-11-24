package com.example.alcdiary.domain.model.user;

import com.example.alcdiary.domain.exception.AlcException;
import com.example.alcdiary.domain.exception.error.CommonError;

public enum EUserAgeRange {

    UNDER_TEN,
    TEN_TO_FOURTEEN,
    FIFTEEN_TO_NINETEEN,
    TWENTIES,
    THIRTIES,
    FORTIES,
    FIFTIES,
    SIXTIES,
    SEVENTIES,
    EIGHTIES,
    OVER_NINETY,
    UNKNOWN;

    public static EUserAgeRange from(String s) {
        switch (s) {
            case "1~9":
                return UNDER_TEN;
            case "10~14":
                return TEN_TO_FOURTEEN;
            case "15~19":
                return FIFTEEN_TO_NINETEEN;
            case "20~29":
                return TWENTIES;
            case "30~39":
                return THIRTIES;
            case "40~49":
                return FORTIES;
            case "50~59":
                return FIFTIES;
            case "60~69":
                return SIXTIES;
            case "70~79":
                return SEVENTIES;
            case "80~89":
                return EIGHTIES;
            case "90~":
                return OVER_NINETY;
            default:
                return UNKNOWN;
        }
    }
}
