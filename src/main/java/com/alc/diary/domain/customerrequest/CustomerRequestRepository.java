package com.alc.diary.domain.customerrequest;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CustomerRequestRepository extends JpaRepository<CustomerRequest, Long> {

    List<CustomerRequest> findByUserId(long userId);
}
