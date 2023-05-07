package com.alc.diary.domain.calender.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Arrays;
import java.util.List;

@Embeddable
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CalenderImage {

    @Column(name = "image_urls", length = 5000)
    private String urls;

    public CalenderImage(List<String> images) {
        this.urls = StringUtils.join(images, ",");
    }

    public List<String> getImages() {
        return Arrays.asList(this.urls.split(","));
    }
}
