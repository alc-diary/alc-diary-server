package com.alc.diary.application.admin.pushmessage;

import com.alc.diary.domain.exception.DomainException;
import com.alc.diary.domain.pushmessage.PushMessage;
import com.alc.diary.domain.pushmessage.PushMessageError;
import com.alc.diary.domain.pushmessage.PushMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class AdminPushMessageServiceV1 {

    private final PushMessageRepository pushMessageRepository;

    public AdminPushMessageDto create(AdminCreatePushMessageRequestV1 request) {
        PushMessage newPushMessage = PushMessage.create(request.title(), request.body(), request.eventName());
        PushMessage savedPushMessage = pushMessageRepository.save(newPushMessage);

        return AdminPushMessageDto.fromDomain(savedPushMessage);
    }

    public Page<AdminPushMessageDto> getAll(Pageable pageable) {
        return pushMessageRepository.findAll(pageable)
                .map(AdminPushMessageDto::fromDomain);
    }

    public AdminPushMessageDto getById(long pushMessageId) {
        return pushMessageRepository.findById(pushMessageId)
                .map(AdminPushMessageDto::fromDomain)
                .orElseThrow(() -> new DomainException(PushMessageError.NOT_FOUND));
    }
}
