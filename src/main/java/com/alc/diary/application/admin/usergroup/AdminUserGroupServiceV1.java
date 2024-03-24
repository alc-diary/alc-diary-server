package com.alc.diary.application.admin.usergroup;

import com.alc.diary.domain.exception.DomainException;
import com.alc.diary.domain.user.User;
import com.alc.diary.domain.user.UserGroup;
import com.alc.diary.domain.user.error.UserError;
import com.alc.diary.domain.user.error.UserGroupError;
import com.alc.diary.domain.user.repository.UserGroupMembershipRepository;
import com.alc.diary.domain.user.repository.UserGroupRepository;
import com.alc.diary.domain.user.repository.UserRepository;
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

    private final UserRepository userRepository;
    private final UserGroupMembershipRepository userGroupMembershipRepository;

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
                .map(AdminUserGroupDto::fromDomainWithUsers)
                .orElseThrow(() -> new DomainException(UserGroupError.NOT_FOUND));
    }

    @Transactional
    public void addUserToGroup(long userGroupId, AdminAddUserToGroupRequestV1 request) {
        UserGroup userGroup = userGroupRepository.findById(userGroupId)
                .orElseThrow(() -> new DomainException(UserGroupError.NOT_FOUND));
        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new DomainException(UserError.USER_NOT_FOUND));

        if (userGroupMembershipRepository.existsByUserAndUserGroup(user, userGroup)) {
            return;
        }

        userGroup.addUser(user);
    }
}
