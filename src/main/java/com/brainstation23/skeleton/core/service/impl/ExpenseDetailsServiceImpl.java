package com.brainstation23.skeleton.core.service.impl;

import com.brainstation23.skeleton.core.domain.enums.ResponseMessage;
import com.brainstation23.skeleton.core.domain.exceptions.InvalidRequestDataException;
import com.brainstation23.skeleton.core.domain.exceptions.RecordNotFoundException;
import com.brainstation23.skeleton.core.domain.request.ExpenseDetailsRequest;
import com.brainstation23.skeleton.core.domain.request.UpdateExpenseDetailsRequest;
import com.brainstation23.skeleton.core.domain.request.UpdateExpenseRequest;
import com.brainstation23.skeleton.core.domain.response.ExpenseDetailsResponse;
import com.brainstation23.skeleton.core.service.BaseService;
import com.brainstation23.skeleton.core.service.ExpenseManagentService;
import com.brainstation23.skeleton.data.entity.expense.EventExpenseInvoice;
import com.brainstation23.skeleton.data.entity.expense.IndividualEventExpense;
import com.brainstation23.skeleton.data.repository.expense.EventExpenseInvoiceRepository;
import com.brainstation23.skeleton.data.repository.expense.IndividualEventExpenseRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExpenseDetailsServiceImpl extends BaseService{
    private final IndividualEventExpenseRepository individualEventExpenseRepository;
    private final EventExpenseInvoiceRepository eventExpenseInvoiceRepository;
    private final ExpenseManagentService expenseManagementService;

    @Transactional
    public void createEventExpenseInvoice(ExpenseDetailsRequest request){
        validateRequest(request);
        IndividualEventExpense individualEventExpense=findIndividualExpenseForEvent(request);
        EventExpenseInvoice expenseDetails = createExpenseDetails(request,individualEventExpense.getId());
        eventExpenseInvoiceRepository.save(expenseDetails);
        updateIndividualExpenseForEvent(request.getSpentAmount(),individualEventExpense.getId());
    }

    @Transactional
    public void updateEventExpenseInvoice(UpdateExpenseDetailsRequest request){
        EventExpenseInvoice expenseDetails = findExpenseDetails(request);
        updateRequiredFields(expenseDetails,request);
        eventExpenseInvoiceRepository.save(expenseDetails);
    }

    public List<ExpenseDetailsResponse> getExpenseDetailsForUser(Long eventExpenseId){
        validateExpenseDetailsRequest(eventExpenseId);
        List<EventExpenseInvoice> eventExpenseInvoices=getAllExpenseViaEventExpenseId(eventExpenseId);
        return eventExpenseInvoices.stream()
                .map(this::mapToExpenseDetailsResponse)
                .collect(Collectors.toList());
    }

    private void validateExpenseDetailsRequest(Long eventExpenseId) {
        String userName =getCurrentUserContext().getUsername();
        IndividualEventExpense individualEventExpense = individualEventExpenseRepository.findById(eventExpenseId).orElseThrow(
                () -> new RecordNotFoundException(ResponseMessage.RECORD_NOT_FOUND));
        if(!individualEventExpense.getUserName().equals(userName)) {
            throw new InvalidRequestDataException(ResponseMessage.UNAUTHORIZED_RESOURCE_ACCESS);
        }
    }

    ExpenseDetailsResponse mapToExpenseDetailsResponse(EventExpenseInvoice eventExpenseInvoice){
        return ExpenseDetailsResponse.builder()
                .amount(eventExpenseInvoice.getAmount())
                .expenseId(eventExpenseInvoice.getId())
                .category(eventExpenseInvoice.getCategory())
                .currency(eventExpenseInvoice.getCurrency())
                .build();
    }

    private List<EventExpenseInvoice> getAllExpenseViaEventExpenseId(Long eventExpenseId) {
        return eventExpenseInvoiceRepository.findAllByIndividualExpenseId(eventExpenseId);
    }

    @Transactional
    void updateRequiredFields(EventExpenseInvoice expenseDetails, UpdateExpenseDetailsRequest request) {
        if(request.getSpentAmount()>0) {
            double previousAmount = expenseDetails.getAmount();
            expenseDetails.setAmount(request.getSpentAmount());
            double difference = request.getSpentAmount() - previousAmount;
            updateIndividualExpenseForEvent(difference,expenseDetails.getIndividualExpenseId());
        }
        if(!StringUtils.isBlank(request.getCategory())) {
            expenseDetails.setCategory(request.getCategory());
        }
    }

    private EventExpenseInvoice findExpenseDetails(UpdateExpenseDetailsRequest request) {
        if(Objects.isNull(request)){
            throw new InvalidRequestDataException(ResponseMessage.INVALID_REQUEST_DATA);
        }
        if(Objects.isNull(request.getExpenseId()) ){
            throw new InvalidRequestDataException(ResponseMessage.INVALID_REQUEST_DATA);
        }
        return eventExpenseInvoiceRepository.findById(request.getExpenseId()).orElseThrow(
                ()->new RecordNotFoundException(ResponseMessage.RECORD_NOT_FOUND)
        );
    }

    private IndividualEventExpense findIndividualExpenseForEvent(ExpenseDetailsRequest request) {
        return individualEventExpenseRepository.findByEventIdAndUserName(request.getEventId(),getCurrentUserContext().getUsername()).orElseThrow(
                ()->new RecordNotFoundException(ResponseMessage.RECORD_NOT_FOUND));
    }

    @Transactional
    void updateIndividualExpenseForEvent(double spentAmount,Long id) {
        UpdateExpenseRequest updateExpenseRequest=buildUpdateExpenseRequest(id,spentAmount);
        expenseManagementService.updateIndividualExpense(updateExpenseRequest);
    }

    private UpdateExpenseRequest buildUpdateExpenseRequest(Long id, double spentAmount) {
        return UpdateExpenseRequest.builder()
                .individualExpenseId(id)
                .spentAmount(spentAmount)
                .build();
    }
    private EventExpenseInvoice createExpenseDetails(ExpenseDetailsRequest request,Long id) {
        return EventExpenseInvoice.builder()
                .individualExpenseId(id)
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
