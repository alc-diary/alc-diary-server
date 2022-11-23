package com.example.alcdiary.application.kakao.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class KakaoResponse {
    private Long id;
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

        private Boolean profile_image_needs_agreement;
        private Profile profile;
        private Boolean has_email;
        private Boolean email_needs_agreement;
        private Boolean is_email_valid;
        private Boolean is_email_verified;
        private String email;

        @Getter
        public static class Profile {

            private String thumbnail_image_url;
            private String profile_image_url;
            private Boolean is_default_image;
        }

    }
}
