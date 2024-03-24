package com.alc.diary.application.admin.usergroup;

import com.alc.diary.application.admin.user.UserDto;
import com.alc.diary.domain.user.UserGroup;
import com.alc.diary.domain.user.UserGroupMembership;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record AdminUserGroupDto(

        long id,
        String name,
        String description,
        List<UserDto> users,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {

    public static AdminUserGroupDto fromDomain(UserGroup domain) {
        return new AdminUserGroupDto(
                domain.getId(),
                domain.getName(),
                domain.getDescription(),
                null,
                domain.getCreatedAt(),
                domain.getUpdatedAt());
    }

    public static AdminUserGroupDto fromDomainWithUsers(UserGroup domain) {
        return new AdminUserGroupDto(
                domain.getId(),
                domain.getName(),
                domain.getDescription(),
                domain.getMemberships().stream()
                        .map(UserGroupMembership::getUser)
                        .map(UserDto::fromDomainModel)
                        .toList(),
                domain.getCreatedAt(),
                domain.getUpdatedAt());
    }
}
