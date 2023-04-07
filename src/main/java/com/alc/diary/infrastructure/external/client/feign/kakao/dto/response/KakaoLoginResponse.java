package com.alc.diary.infrastructure.external.client.feign.kakao.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
public class KakaoLoginResponse {

    private long id;

    @JsonProperty("connected_at")
    private LocalDateTime connectedAt;

    private Properties properties;

    @JsonProperty("kakao_account")
    private KakaoAccount kakaoAccount;

    @Getter
    public static class Properties {

        @JsonProperty("profile_image")
        private String profileImage;

        @JsonProperty("thumbnail_image")
        private String thumbnailImage;
    }

    @Getter
    public static class KakaoAccount {

        @JsonProperty("profile_image_needs_agreement")
        private boolean profileImageNeedsAgreement;

        private Profile profile;

        @JsonProperty("has_email")
        private boolean has_email;

        @JsonProperty("email_needs_agreement")
        private boolean emailNeedsAgreement;

        @JsonProperty("is_email_valid")
        private boolean isEmailValid;

        @JsonProperty("is_email_verified")
        private boolean isEmailVerified;

        private String email;

        @JsonProperty("has_age_range")
        private boolean hasAgeAange;

        @JsonProperty("age_range_needs_agreement")
        private boolean ageRangeNeedsAgreement;

        @JsonProperty("age_range")
        private String ageRange;

        @JsonProperty("has_gender")
        private boolean hasGender;

        @JsonProperty("gender_needs_agreement")
        private boolean genderNeedsAgreement;

        private String gender;

        @Getter
        public static class Profile {

            @JsonProperty("thumbnail_image_url")
            private String thumbnailImageUrl;

            @JsonProperty("profile_image_url")
            private String profileImageUrl;

            @JsonProperty("is_default_image")
            private String isDefaultImage;
        }
    }
}
