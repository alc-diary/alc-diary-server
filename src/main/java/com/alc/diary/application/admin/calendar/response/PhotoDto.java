package com.alc.diary.application.admin.calendar.response;

import com.alc.diary.domain.calendar.Photo;

import java.time.LocalDateTime;

public record PhotoDto(

        long id,
        long userId,
        String url,
        LocalDateTime deletedAt
) {

    public static PhotoDto fromDomainModel(Photo photo) {
        return new PhotoDto(
                photo.getId(),
                photo.getUserId(),
                photo.getUrl(),
                photo.getDeletedAt()
        );
    }
}
