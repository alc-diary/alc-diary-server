package com.alc.diary.application.customerrequest;

import com.alc.diary.domain.customerrequest.CustomerRequest;
import com.alc.diary.domain.customerrequest.CustomerRequestError;
import com.alc.diary.domain.customerrequest.CustomerRequestRepository;
import com.alc.diary.domain.exception.DomainException;
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

    @Transactional
    public CustomerRequestDto create(long userId, CreateCustomerRequestRequestV1 request) {
        CustomerRequest newCustomerRequest =
                CustomerRequest.create(userId, request.serviceSatisfactionLevel(), request.requestContent());
        CustomerRequest savedCustomerRequest = customerRequestRepository.save(newCustomerRequest);

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
