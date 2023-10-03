package com.alc.diary.testuils;

import org.apache.commons.lang3.RandomStringUtils;

import java.util.Random;

public class RandomStringGenerator {

    private static final String ENGLISH_ALPHABET_AND_DIGITS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int KOREAN_UNICODE_START = 0xAC00;
    private static final int KOREAN_UNICODE_END = 0xD7A3;
    private static final Random random = new Random();

    public static String generateRandomString(int length) {
        StringBuilder result = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            double randomValue = random.nextDouble();
            if (randomValue < 0.5) {
                result.append(RandomStringUtils.random(1, ENGLISH_ALPHABET_AND_DIGITS));
            } else {
                int unicode = KOREAN_UNICODE_START + random.nextInt(KOREAN_UNICODE_END - KOREAN_UNICODE_START);
                result.append((char) unicode);
            }
        }
        return result.toString();
    }

    public static String generateRandomLengthString(int maxLength) {
        int length = random.nextInt(maxLength) + 1;
        StringBuilder result = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            double randomValue = random.nextDouble();
            if (randomValue < 0.5) {
                result.append(RandomStringUtils.random(1, ENGLISH_ALPHABET_AND_DIGITS));
            } else {
                int unicode = KOREAN_UNICODE_START + random.nextInt(KOREAN_UNICODE_END - KOREAN_UNICODE_START);
                result.append((char) unicode);
            }
        }
        return result.toString();
    }
}
