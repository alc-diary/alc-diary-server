package com.alc.diary.application.admin.usergroup;

import com.alc.diary.domain.exception.DomainException;
import com.alc.diary.domain.user.UserGroup;
import com.alc.diary.domain.user.error.UserGroupError;
import com.alc.diary.domain.user.repository.UserGroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class AdminUserGroupServiceV1 {

    private final UserGroupRepository userGroupRepository;

    @Transactional
    public AdminUserGroupDto create(AdminCreateUserGroupRequestV1 request) {
        UserGroup newUserGroup = UserGroup.create(request.name(), request.description());
        UserGroup savedUserGroup = userGroupRepository.save(newUserGroup);

        return AdminUserGroupDto.fromDomain(savedUserGroup);
    }

    public Page<AdminUserGroupDto> getAll(Pageable pageable) {
        return userGroupRepository.findAll(pageable)
                .map(AdminUserGroupDto::fromDomain);
    }

    public AdminUserGroupDto getById(long userGroupId) {
        return userGroupRepository.findById(userGroupId)
                .map(AdminUserGroupDto::fromDomain)
                .orElseThrow(() -> new DomainException(UserGroupError.NOT_FOUND));
    }
}
