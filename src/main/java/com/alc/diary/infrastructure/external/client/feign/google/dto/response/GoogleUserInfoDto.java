package com.alc.diary.infrastructure.external.client.feign.google.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record GoogleUserInfoDto(
        String id,
        String email,
        Boolean verified_email,
        String name,
        @JsonProperty("given_name") String givenName,
        @JsonProperty("family_name") String familyName,
        String picture,
        String locale
) {
}
