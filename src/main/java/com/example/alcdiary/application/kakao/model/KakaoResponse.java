package com.example.alcdiary.application.kakao.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class KakaoResponse {
    private long id;
    private LocalDateTime connected_at;
    private Properties properties;
    private KakaoAccount kakao_account;

    @Getter
    public static class Properties {

        private String profile_image;
        private String thumbnail_image;
    }

    @Getter
    public static class KakaoAccount {

        private boolean profile_image_needs_agreement;
        private Profile profile;
        private boolean has_email;
        private boolean email_needs_agreement;
        private boolean is_email_valid;
        private boolean is_email_verified;
        private String email;
        private boolean has_age_range;
        private boolean age_range_needs_agreement;
        private String age_range;
        private boolean has_gender;
        private boolean gender_needs_agreement;
        private String gender;

        @Getter
        public static class Profile {

            private String thumbnail_image_url;
            private String profile_image_url;
            private boolean is_default_image;
        }

    }
}
