package com.brainstation23.skeleton.core.service.impl;

import com.brainstation23.skeleton.core.domain.enums.ResponseMessage;
import com.brainstation23.skeleton.core.domain.exceptions.InvalidRequestDataException;
import com.brainstation23.skeleton.core.domain.exceptions.RecordNotFoundException;
import com.brainstation23.skeleton.core.domain.request.IndividualExpenseRequest;
import com.brainstation23.skeleton.core.domain.request.UpdateExpenseRequest;
import com.brainstation23.skeleton.core.domain.request.UpdateIndividualExpense;
import com.brainstation23.skeleton.core.domain.response.IndividualExpenseResponse;
import com.brainstation23.skeleton.core.service.BaseService;
import com.brainstation23.skeleton.core.service.EventAndExpenseCommonService;
import com.brainstation23.skeleton.core.service.ExpenseManagentService;
import com.brainstation23.skeleton.core.service.GroupEventService;
import com.brainstation23.skeleton.data.entity.expense.IndividualEventExpense;
import com.brainstation23.skeleton.data.repository.expense.EventExpenseInvoiceRepository;
import com.brainstation23.skeleton.data.repository.expense.IndividualEventExpenseRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExpenseManagementServiceImpl extends BaseService implements ExpenseManagentService {

    private final IndividualEventExpenseRepository individualEventExpenseRepository;
    private final EventAndExpenseCommonService eventAndExpenseCommonService;

    @Transactional
    @Override
    public void createIndividualExpense(IndividualExpenseRequest request) {
        validateRequest(request);
        logger.trace("Create individual expense request received: "+ request.toString());
        IndividualEventExpense individualEventExpense = createIndividualExpenseObject(request);
        individualEventExpenseRepository.save(individualEventExpense);
    }

    @Transactional
    @Override
    public void updateIndividualExpense(UpdateExpenseRequest request) {
        IndividualEventExpense individualEventExpense = validateUpdateRequest(request);
        logger.trace("Update individual expense request received: "+ request.toString());
        double totalSpentAmount = request.getSpentAmount()+individualEventExpense.getSpentAmount();
        individualEventExpense.setSpentAmount(totalSpentAmount);
        individualEventExpenseRepository.save(individualEventExpense);
        eventAndExpenseCommonService.updateEventExpense(individualEventExpense.getEventId(), new BigDecimal(totalSpentAmount));
    }

    @Override
    public void updateIndividualPaymentStatus(UpdateIndividualExpense request) {
        IndividualEventExpense individualEventExpense=findIndividualExpense(request);
        individualEventExpense.setPaymentStatus("COMPLETED");
        individualEventExpenseRepository.save(individualEventExpense);
    }

    @Override
    public void updateEditableOption(UpdateIndividualExpense request) {
        IndividualEventExpense individualEventExpense=findIndividualExpense(request);
        individualEventExpense.setIsEditable(Boolean.FALSE);
        individualEventExpenseRepository.save(individualEventExpense);
    }

    @Override
    public IndividualExpenseResponse getIndividualExpense(String eventId) {
        UpdateIndividualExpense request=buildUpdateIndividualExpense(eventId);
        IndividualEventExpense individualEventExpense=findIndividualExpense(request);
        return mapToIndividualExpenseResponse(individualEventExpense);
    }

    private UpdateIndividualExpense buildUpdateIndividualExpense(String eventId) {
        return UpdateIndividualExpense.builder()
                .eventId(eventId)
                .build();
    }

    @Override
    public List<IndividualExpenseResponse> getEventExpenseInvoices(String eventId) {
        List<IndividualEventExpense> individualEventExpenses = getAllIndividualExpenseForEvent(eventId);
        return individualEventExpenses.stream()
                .map(this::mapToIndividualExpenseResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public void deleteIndividualExpense(String eventId, String username) {
        IndividualEventExpense byEventIdAndUserName = individualEventExpenseRepository.findByEventIdAndUserName(eventId, username)
                .orElseThrow(
                        ()-> new RecordNotFoundException(ResponseMessage.RECORD_NOT_FOUND)
                );
        individualEventExpenseRepository.deleteById(byEventIdAndUserName.getId());
    }

    private List<IndividualEventExpense> getAllIndividualExpenseForEvent(String eventId) {
        if(StringUtils.isBlank(eventId)){
            throw new InvalidRequestDataException(ResponseMessage.INVALID_REQUEST_DATA);
        }
        return individualEventExpenseRepository.findAllByEventId(eventId);
    }

    private IndividualExpenseResponse mapToIndividualExpenseResponse(IndividualEventExpense individualEventExpense) {
        return IndividualExpenseResponse.builder()
                .userName(individualEventExpense.getUserName())
                .userIdentity(individualEventExpense.getUserIdentity())
                .currency(individualEventExpense.getCurrency())
                .paymentStatus(individualEventExpense.getPaymentStatus())
                .budgetAmount(individualEventExpense.getBudgetAmount())
                .dueAmount(individualEventExpense.getDueAmount())
                .outstandingBalance(individualEventExpense.getOutstandingBalance())
                .spentAmount(individualEventExpense.getSpentAmount())
                .build();
    }

    private IndividualEventExpense findIndividualExpense(UpdateIndividualExpense request) {
        validateEventId(request);
        return individualEventExpenseRepository.findByEventIdAndUserName(request.getEventId(),getCurrentUserContext().getUsername()).orElseThrow(
                () -> new RecordNotFoundException(ResponseMessage.RECORD_NOT_FOUND)
        );
    }

    private void validateEventId(UpdateIndividualExpense request) {
        if(StringUtils.isBlank(request.getEventId())){
            throw new InvalidRequestDataException(ResponseMessage.INVALID_REQUEST_DATA);
        }
    }


    private IndividualEventExpense validateUpdateRequest(UpdateExpenseRequest request) {
        if(Objects.isNull(request))
            throw new InvalidRequestDataException(ResponseMessage.INVALID_REQUEST_DATA);
        return validateIndividualExpense(request);
    }

    private IndividualEventExpense validateIndividualExpense(UpdateExpenseRequest request) {
        return individualEventExpenseRepository.findById(request.getIndividualExpenseId()).orElseThrow(
                () -> new RecordNotFoundException(ResponseMessage.RECORD_NOT_FOUND));
    }

    private IndividualEventExpense createIndividualExpenseObject(IndividualExpenseRequest request) {
        return IndividualEventExpense.builder()
                .eventId(request.getEventId())
                .currency("BDT")
                .spentAmount(0.00)
                .outstandingBalance(0.00)
                .dueAmount(0.00)
                .budgetAmount(0.00)
                .userIdentity(request.getUserIdentity())
                .userName(request.getUserName())
                .groupId(request.getGroupId())
                .isEditable(true)
                .paymentStatus("PENDING")
                .createdAt(new Date())
                .build();
    }

    private void validateRequest(IndividualExpenseRequest request) {
        if(Objects.isNull(request)) {
            throw new InvalidRequestDataException(ResponseMessage.INVALID_REQUEST_DATA);
        }
        if (StringUtils.isBlank(request.getEventId()) || StringUtils.isBlank(request.getUserIdentity())
        || StringUtils.isBlank(request.getUserName()) || StringUtils.isBlank(request.getGroupId())){
            throw new InvalidRequestDataException(ResponseMessage.INVALID_REQUEST_DATA);
        }
    }
}
