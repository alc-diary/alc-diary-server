package com.alc.diary.application.customerrequest;

import com.alc.diary.application.notification.NotificationService;
import com.alc.diary.domain.customerrequest.CustomerRequest;
import com.alc.diary.domain.customerrequest.CustomerRequestError;
import com.alc.diary.domain.customerrequest.CustomerRequestRepository;
import com.alc.diary.domain.exception.DomainException;
import com.alc.diary.domain.user.UserGroup;
import com.alc.diary.domain.user.UserGroupMembership;
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
public class CustomerRequestServiceV1 {

    private final CustomerRequestRepository customerRequestRepository;

    private final UserGroupRepository userGroupRepository;
    private final NotificationService notificationService;

    @Transactional
    public CustomerRequestDto create(long userId, CreateCustomerRequestRequestV1 request) {
        CustomerRequest newCustomerRequest =
                CustomerRequest.create(userId, request.serviceSatisfactionLevel(), request.requestContent());
        CustomerRequest savedCustomerRequest = customerRequestRepository.save(newCustomerRequest);

        UserGroup adminUserGroup = userGroupRepository.findByName("ADMIN")
                .orElseThrow(() -> new DomainException(UserGroupError.NOT_FOUND));
        for (UserGroupMembership membership : adminUserGroup.getMemberships()) {
            notificationService.sendFcm(membership.getUser().getId(), "유저 요청이 등록됐어요!", "확인", "TEST_EVENT");
        }

        return CustomerRequestDto.fromDomain(savedCustomerRequest);
    }

    public Page<CustomerRequestDto> getAll(Pageable pageable) {
        return customerRequestRepository.findAll(pageable)
                .map(CustomerRequestDto::fromDomain);
    }

    public CustomerRequestDto getById(long id) {
        return customerRequestRepository.findById(id)
                .map(CustomerRequestDto::fromDomain)
                .orElseThrow(() -> new DomainException(CustomerRequestError.NOT_FOUND));
    }
}
