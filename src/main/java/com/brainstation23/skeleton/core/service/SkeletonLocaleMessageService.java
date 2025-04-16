package com.brainstation23.skeleton.core.service;

import com.brainstation23.skeleton.core.domain.exceptions.RecordNotFoundException;
import com.brainstation23.skeleton.core.domain.model.LocaleMessageDto;
import com.brainstation23.skeleton.data.entity.LocaleMessage;
import com.brainstation23.skeleton.data.entity.redis.Message;
import com.brainstation23.skeleton.core.domain.enums.ResponseMessage;
import com.brainstation23.skeleton.data.repository.LocaleMessageRepository;
import com.brainstation23.skeleton.data.repository.redis.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SkeletonLocaleMessageService extends BaseService {

    private final LocaleMessageRepository localeMessageRepository;
    private final MessageRepository messageRepository;

    public LocaleMessageDto getLocaleData(final String locale) {

        final LocaleMessage localeMessage = localeMessageRepository.findLocaleMessageByLocale(locale)
                .orElseThrow(() -> new RecordNotFoundException(ResponseMessage.LOCALE_RECORD_NOT_FOUND));

        return new LocaleMessageDto(localeMessage.getLocale(), localeMessage.getContent());
    }

    public Optional<Message> findById(String id) {
        return messageRepository.findById(id);
    }

}
