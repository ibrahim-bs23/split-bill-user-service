package com.brainstation23.skeleton.core.service.impl;

import com.brainstation23.skeleton.core.domain.enums.ResponseMessage;
import com.brainstation23.skeleton.core.domain.exceptions.InvalidRequestDataException;
import com.brainstation23.skeleton.core.domain.exceptions.RecordNotFoundException;
import com.brainstation23.skeleton.core.domain.request.ExpenseDetailsRequest;
import com.brainstation23.skeleton.core.domain.request.IndividualExpenseRequest;
import com.brainstation23.skeleton.core.domain.request.UpdateExpenseRequest;
import com.brainstation23.skeleton.core.service.BaseService;
import com.brainstation23.skeleton.core.service.ExpenseManagentService;
import com.brainstation23.skeleton.data.entity.expense.IndividualEventExpense;
import com.brainstation23.skeleton.data.repository.expense.IndividualEventExpenseRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ExpenseManagementServiceImpl extends BaseService implements ExpenseManagentService {

    private final IndividualEventExpenseRepository individualEventExpenseRepository;

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
        individualEventExpense.setSpentAmount(request.getSpentAmount());
        individualEventExpenseRepository.save(individualEventExpense);

        //TODO: Update the expense in the event
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
                .currency("TK")
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
