package com.alc.diary.domain.calender.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@ToString
@Embeddable
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CalenderImage {

    @Column(name = "image_urls", length = 1024)
    private String urls;

    public CalenderImage(List<String> images) {
        if (!isEmpty(images)) {
            urls = StringUtils.join(images, ",");
        }
    }

    private static boolean isEmpty(List<String> images) {
        return images == null || images.isEmpty();
    }

    public List<String> getImages() {
        if (!StringUtils.isEmpty(urls)) {
            return Arrays.asList(this.urls.split(","));
        }
        return new ArrayList<>();
    }
}
