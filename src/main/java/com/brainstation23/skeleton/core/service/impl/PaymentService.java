package com.brainstation23.skeleton.core.service.impl;

import com.brainstation23.skeleton.core.domain.enums.RequestMoneyStatusEnum;
import com.brainstation23.skeleton.core.domain.enums.ResponseMessage;
import com.brainstation23.skeleton.core.domain.exceptions.RecordNotFoundException;
import com.brainstation23.skeleton.core.service.BaseService;
import com.brainstation23.skeleton.data.entity.ExpenseSplit;
import com.brainstation23.skeleton.data.entity.expense.IndividualEventExpense;
import com.brainstation23.skeleton.data.repository.ExpenseSplitRepository;
import com.brainstation23.skeleton.data.repository.expense.IndividualEventExpenseRepository;
import com.brainstation23.skeleton.presenter.domain.request.user.PaymentRequest;
import com.brainstation23.skeleton.presenter.service.nonTransactional.NonTransactionalIntegrationService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PaymentService extends BaseService {

    private final IndividualEventExpenseRepository individualEventExpenseRepository;
    private final ExpenseSplitRepository expenseSplitRepository;
    private final NonTransactionalIntegrationService nonTransactionalIntegrationService;


    public void initiatePaymentProcess(PaymentRequest paymentRequest) {
        String senderUsername = paymentRequest.getSenderUsername();
        String receiverUsername = paymentRequest.getReceiverUsername();
        Double amount = paymentRequest.getAmount();
        String eventId = paymentRequest.getEventId();
        updateSenderIndividualExpenseReport(senderUsername,amount,eventId);
        updateReceiverIndividualExpenseReport(receiverUsername,amount,eventId);
        updateExpenseSplitRequestStatus(senderUsername,receiverUsername,eventId);
        updatePaymentHistory(paymentRequest.getTransactionId());
    }

    private void updatePaymentHistory(@NotNull String transactionId) {
        nonTransactionalIntegrationService.updatePaymentHistory(transactionId);
    }

    public void updateExpenseSplitRequestStatus(String senderUsername, String receiverUsername, String eventId) {
        final Optional<ExpenseSplit> expenseSplitOptional
                = expenseSplitRepository.findByEventIdAndSenderUserNameAndReceiverUserName(
                eventId,
                senderUsername,
                receiverUsername);

        if (expenseSplitOptional.isPresent())
        {
            expenseSplitOptional.get().setRequestStatus(RequestMoneyStatusEnum.ACCEPTED.toString());
            expenseSplitRepository.save(expenseSplitOptional.get());
        }
    }


    public void updateReceiverIndividualExpenseReport(String receiverUsername, Double amount, String eventId) {
        IndividualEventExpense individualEventExpense = individualEventExpenseRepository.findByEventIdAndUserName(eventId, receiverUsername).orElseThrow(
                () -> new RecordNotFoundException(ResponseMessage.RECORD_NOT_FOUND)
        );
        double currentOutStanding=individualEventExpense.getOutstandingBalance()-amount;
        individualEventExpense.setOutstandingBalance(currentOutStanding);
        if(individualEventExpense.getDueAmount()==0 && individualEventExpense.getOutstandingBalance()>=0 && individualEventExpense.getOutstandingBalance()<1) {
            individualEventExpense.setPaymentStatus("COMPLETED");
        }
        individualEventExpenseRepository.save(individualEventExpense);
    }


    public void updateSenderIndividualExpenseReport(String senderUsername, Double amount,String eventId) {
        IndividualEventExpense individualEventExpense = individualEventExpenseRepository.findByEventIdAndUserName(eventId, senderUsername).orElseThrow(
                () -> new RecordNotFoundException(ResponseMessage.RECORD_NOT_FOUND)
        );
        double currentDueAmount = individualEventExpense.getDueAmount()-amount;
        individualEventExpense.setDueAmount(currentDueAmount);
        if(individualEventExpense.getDueAmount()>=0 && individualEventExpense.getDueAmount()<1 && individualEventExpense.getOutstandingBalance()==0) {
            individualEventExpense.setPaymentStatus("COMPLETED");
        }
        individualEventExpenseRepository.save(individualEventExpense);
    }
}
