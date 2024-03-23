package com.alc.diary.application.admin.usergroup;

import com.alc.diary.domain.user.UserGroup;

import java.time.LocalDateTime;

public record AdminUserGroupDto(

        long id,
        String name,
        String description,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {

    public static AdminUserGroupDto fromDomain(UserGroup domain) {
        return new AdminUserGroupDto(
                domain.getId(),
                domain.getName(),
                domain.getDescription(),
                domain.getCreatedAt(),
                domain.getUpdatedAt());
    }
}
