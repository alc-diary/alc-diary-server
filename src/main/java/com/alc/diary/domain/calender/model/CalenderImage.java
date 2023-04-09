package com.alc.diary.domain.calender.model;

import lombok.Builder;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
@Builder
public class CalenderImage {

    @Column(name = "image_urls", length = 5000)
    private String urls;

    public CalenderImage() {

    }
}
