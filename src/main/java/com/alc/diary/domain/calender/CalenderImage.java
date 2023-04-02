package com.alc.diary.domain.calender;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class CalenderImage {

    @Column(name = "image_urls", length = 5000)
    private String urls;
}
