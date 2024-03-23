package com.alc.diary.presentation.api.admin;

import com.alc.diary.application.admin.usergroup.AdminAddUserToGroupRequestV1;
import com.alc.diary.application.admin.usergroup.AdminCreateUserGroupRequestV1;
import com.alc.diary.application.admin.usergroup.AdminUserGroupDto;
import com.alc.diary.application.admin.usergroup.AdminUserGroupServiceV1;
import com.alc.diary.presentation.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/admin/v1/user-groups")
@RestController
public class AdminUserGroupControllerV1 {

    private final AdminUserGroupServiceV1 adminUserGroupService;

    @PostMapping
    public ApiResponse<AdminUserGroupDto> create(@RequestBody AdminCreateUserGroupRequestV1 request) {
        return ApiResponse.getCreated(adminUserGroupService.create(request));
    }

    @GetMapping
    public ApiResponse<Page<AdminUserGroupDto>> getAll(Pageable pageable) {
        return ApiResponse.getSuccess(adminUserGroupService.getAll(pageable));
    }

    @GetMapping("/{userGroupId}")
    public ApiResponse<AdminUserGroupDto> getById(@PathVariable long userGroupId) {
        return ApiResponse.getSuccess(adminUserGroupService.getById(userGroupId));
    }

    @PostMapping("/{userGroupId}/users")
    public ApiResponse<Void> addUserToGroup(
            @PathVariable long userGroupId, @RequestBody AdminAddUserToGroupRequestV1 request) {
        adminUserGroupService.addUserToGroup(userGroupId, request);
        return ApiResponse.getSuccess();
    }
}
