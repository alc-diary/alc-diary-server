package com.alc.diary.domain.customerrequest;

import com.alc.diary.domain.BaseCreationEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "customer_requests", indexes = {@Index(name = "idx_customer_request_user_id", columnList = "user_id")})
@Entity
public class CustomerRequest extends BaseCreationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "user_id", nullable = false)
    private long userId;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "service_satisfaction_level", length = 30, nullable = false)
    private ServiceSatisfactionLevel serviceSatisfactionLevel;

    @NotNull
    @Lob
    @Column(name = "request_content", nullable = false)
    private String requestContent;

    private CustomerRequest(Long id, long userId, ServiceSatisfactionLevel serviceSatisfactionLevel, String requestContent) {
        this.id = id;
        this.userId = userId;
        this.serviceSatisfactionLevel = serviceSatisfactionLevel;
        this.requestContent = requestContent;
    }

    public static CustomerRequest create(
            long userId, ServiceSatisfactionLevel serviceSatisfactionLevel, String requestContent) {
        return new CustomerRequest(null, userId, serviceSatisfactionLevel, requestContent);
    }
}
