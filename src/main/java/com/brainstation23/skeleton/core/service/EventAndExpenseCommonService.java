package com.brainstation23.skeleton.core.service;

import com.brainstation23.skeleton.core.domain.enums.ResponseMessage;
import com.brainstation23.skeleton.core.domain.exceptions.RecordNotFoundException;
import com.brainstation23.skeleton.data.entity.GroupEvent;
import com.brainstation23.skeleton.data.repository.GroupEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class EventAndExpenseCommonService {

    private final GroupEventRepository groupEventRepository;
    @Transactional
    public void updateEventExpense(String eventId, BigDecimal expenseAmount) {
        GroupEvent groupEvent = groupEventRepository.findFirstByEventId(eventId)
                .orElseThrow(() -> new RecordNotFoundException(ResponseMessage.EVENT_NOT_FOUND));
        groupEvent.setTotalSpending(groupEvent.getTotalSpending().add(expenseAmount));
        groupEventRepository.save(groupEvent);
    }
}
