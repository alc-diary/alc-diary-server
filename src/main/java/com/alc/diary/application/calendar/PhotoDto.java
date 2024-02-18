package com.alc.diary.application.calendar;

import com.alc.diary.domain.calendar.Photo;

public record PhotoDto(

        long id,
        long userId,
        String url
) {

    public static PhotoDto fromDomainModel(Photo photo) {
        return new PhotoDto(photo.getId(), photo.getUserId(), photo.getUrl());
    }
}
