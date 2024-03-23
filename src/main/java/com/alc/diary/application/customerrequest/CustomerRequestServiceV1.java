package com.alc.diary.application.customerrequest;

import com.alc.diary.application.notification.NotificationService;
import com.alc.diary.domain.customerrequest.CustomerRequest;
import com.alc.diary.domain.customerrequest.CustomerRequestRepository;
import com.alc.diary.domain.exception.DomainException;
import com.alc.diary.domain.user.UserGroup;
import com.alc.diary.domain.user.UserGroupMembership;
import com.alc.diary.domain.user.error.UserGroupError;
import com.alc.diary.domain.user.repository.UserGroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

        sendPushMessageToAdminUsers(savedCustomerRequest);

        return CustomerRequestDto.fromDomain(savedCustomerRequest);
    }

    private void sendPushMessageToAdminUsers(CustomerRequest savedCustomerRequest) {
        UserGroup adminUserGroup = userGroupRepository.findByName("관리자")
                .orElseThrow(() -> new DomainException(UserGroupError.NOT_FOUND));
        for (UserGroupMembership membership : adminUserGroup.getMemberships()) {
            String messageBody = "만족도: " + savedCustomerRequest.getServiceSatisfactionLevel();
            notificationService.sendFcm(
                    membership.getUser().getId(),
                    "고객 요청사항이 등록됐어요!",
                    messageBody,
                    "NEW_CUSTOMER_REQUEST");
        }
    }

    public List<CustomerRequestDto> getByUserId(long userId) {
        return customerRequestRepository.findByUserId(userId).stream()
                .map(CustomerRequestDto::fromDomain)
                .toList();
    }
}
