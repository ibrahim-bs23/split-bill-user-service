package com.brainstation23.skeleton.core.service.impl;

import com.brainstation23.skeleton.core.domain.enums.ResponseMessage;
import com.brainstation23.skeleton.core.domain.exceptions.InvalidRequestDataException;
import com.brainstation23.skeleton.core.domain.exceptions.RecordNotFoundException;
import com.brainstation23.skeleton.core.domain.request.ExpenseDetailsRequest;
import com.brainstation23.skeleton.core.domain.request.UpdateExpenseRequest;
import com.brainstation23.skeleton.core.service.BaseService;
import com.brainstation23.skeleton.core.service.ExpenseManagentService;
import com.brainstation23.skeleton.data.entity.expense.EventExpenseInvoice;
import com.brainstation23.skeleton.data.entity.expense.IndividualEventExpense;
import com.brainstation23.skeleton.data.repository.expense.EventExpenseInvoiceRepository;
import com.brainstation23.skeleton.data.repository.expense.IndividualEventExpenseRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ExpenseDetailsServiceImpl extends BaseService{
    private final IndividualEventExpenseRepository individualEventExpenseRepository;
    private final EventExpenseInvoiceRepository eventExpenseInvoiceRepository;
    private ExpenseManagentService expenseManagementService;

    @Transactional
    void createEventExpenseInvoice(ExpenseDetailsRequest request){
        validateRequest(request);
        EventExpenseInvoice expenseDetails = createExpenseDetails(request);
        eventExpenseInvoiceRepository.save(expenseDetails);
        updateIndividualExpenseForEvent(request);
    }

    private void updateIndividualExpenseForEvent(ExpenseDetailsRequest request) {
        IndividualEventExpense individualEventExpense=getIndividualExpenseEntity(request.getEventId(),getCurrentUserContext().getUserName());
        UpdateExpenseRequest updateExpenseRequest=buildUpdateExpenseRequest(individualEventExpense.getId(),request.getSpentAmount());
        expenseManagementService.updateIndividualExpense(updateExpenseRequest);
    }

    private UpdateExpenseRequest buildUpdateExpenseRequest(Long id, double spentAmount) {
        return UpdateExpenseRequest.builder()
                .individualExpenseId(id)
                .spentAmount(spentAmount)
                .build();
    }

    private IndividualEventExpense getIndividualExpenseEntity(String eventId, String userName) {
        return individualEventExpenseRepository.findByEventIdAndUserName(eventId, userName).orElseThrow(
                ()-> new RecordNotFoundException(ResponseMessage.RECORD_NOT_FOUND));

    }

    private EventExpenseInvoice createExpenseDetails(ExpenseDetailsRequest request) {
        return EventExpenseInvoice.builder()
                .eventId(request.getEventId())
                .createdAt(new Date())
                .amount(request.getSpentAmount())
                .category(request.getCategory())
                .currency("BDT")
                .build();
    }

    private void validateRequest(ExpenseDetailsRequest request) {
        if(Objects.isNull(request)){
            throw new InvalidRequestDataException(ResponseMessage.INVALID_REQUEST_DATA);
        }
       //TODO validate event
    }
}
