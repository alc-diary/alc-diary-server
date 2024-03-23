package com.alc.diary.application.admin.customerrequest;

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
public class AdminCustomerRequestServiceV1 {

    private final CustomerRequestRepository customerRequestRepository;

    public Page<AdminCustomerRequestDto> getAll(Pageable pageable) {
        return customerRequestRepository.findAll(pageable)
                .map(AdminCustomerRequestDto::fromDomain);
    }

    public AdminCustomerRequestDto getById(long id) {
        return customerRequestRepository.findById(id)
                .map(AdminCustomerRequestDto::fromDomain)
                .orElseThrow(() -> new DomainException(CustomerRequestError.NOT_FOUND));
    }
}
