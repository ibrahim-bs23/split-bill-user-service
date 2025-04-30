package com.brainstation23.skeleton.core.service;


import com.brainstation23.skeleton.core.domain.enums.RequestMoneyStatusEnum;
import com.brainstation23.skeleton.core.domain.enums.ResponseMessage;
import com.brainstation23.skeleton.core.domain.exceptions.InvalidRequestDataException;
import com.brainstation23.skeleton.data.entity.ExpenseSplit;
import com.brainstation23.skeleton.data.entity.ExpenseTransaction;
import com.brainstation23.skeleton.data.entity.GroupEvent;
import com.brainstation23.skeleton.data.entity.expense.IndividualEventExpense;
import com.brainstation23.skeleton.data.repository.ExpenseSplitRepository;
import com.brainstation23.skeleton.data.repository.GroupEventRepository;
import com.brainstation23.skeleton.data.repository.expense.IndividualEventExpenseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ExpenseSplitCalculationService extends BaseService {

    private final IndividualEventExpenseRepository individualEventExpenseRepository;
    private final ExpenseSplitRepository expenseSplitRepository;
    private final GroupEventRepository groupEventRepository;

    static class Balance {
        String name;
        String userIdentity;
        BigDecimal amount;

        Balance(String name,String userIdentity, BigDecimal amount) {
            this.name = name;
            this.userIdentity = userIdentity;
            this.amount = amount;
        }
    }

    public List<ExpenseSplit> settleExpenses(String eventId) {

        final Optional<GroupEvent> groupEventOptional = groupEventRepository.findFirstByEventId(eventId);

        if (groupEventOptional.isEmpty()) {
            throw new InvalidRequestDataException(ResponseMessage.EVENT_NOT_FOUND);
        }

        if (!Objects.equals(groupEventOptional.get().getCreatedBy(), getUserIdentity())) {
            throw new InvalidRequestDataException(ResponseMessage.UNAUTHORIZED_RESOURCE_ACCESS);
        }

        List<IndividualEventExpense> expenses = individualEventExpenseRepository.findAllByEventId(eventId);

        boolean isSplittable = expenses.stream()
                .allMatch(expense -> Objects.equals(expense.getIsEditable(), Boolean.FALSE));

        if (!isSplittable) {
            throw new InvalidRequestDataException(ResponseMessage.EVENT_NOT_SPLITTABLE);
        }


        BigDecimal total = expenses.stream()
                .map(expense -> BigDecimal.valueOf(expense.getSpentAmount()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal fairShare = total.divide(BigDecimal.valueOf(expenses.size()), 2, RoundingMode.HALF_UP);

        PriorityQueue<Balance> creditors = new PriorityQueue<>(
                (a, b) -> b.amount.compareTo(a.amount)
        );

        PriorityQueue<Balance> debtors = new PriorityQueue<>(
                Comparator.comparing(a -> a.amount)
        );

        for (IndividualEventExpense expense : expenses) {

            updateIndividualExpense(expense, fairShare, creditors, debtors);

        }


        while (!debtors.isEmpty() && !creditors.isEmpty()) {
            Balance debtor = debtors.poll();
            Balance creditor = creditors.poll();

            BigDecimal settleAmount = debtor.amount.min(creditor.amount);
            final ExpenseSplit expenseSplit = createExpenseSplit(debtor, creditor, eventId, settleAmount);
            expenseSplitRepository.save(expenseSplit);

            BigDecimal debtorLeft = debtor.amount.subtract(settleAmount);
            BigDecimal creditorLeft = creditor.amount.subtract(settleAmount);

            if (debtorLeft.compareTo(BigDecimal.ZERO) > 0) {
                debtors.offer(new Balance(debtor.name,debtor.userIdentity, debtorLeft));
            }

            if (creditorLeft.compareTo(BigDecimal.ZERO) > 0) {
                creditors.offer(new Balance(creditor.name,creditor.userIdentity, creditorLeft));
            }
        }

        return expenseSplitRepository.findAllByEventId(eventId);
    }

    private void updateIndividualExpense(IndividualEventExpense expense, BigDecimal fairShare, PriorityQueue<Balance> creditors, PriorityQueue<Balance> debtors) {
        BigDecimal spentAmount = BigDecimal.valueOf(expense.getSpentAmount());

        BigDecimal net = spentAmount.subtract(fairShare).setScale(2, RoundingMode.HALF_UP);

        if (net.compareTo(BigDecimal.ZERO) > 0) {
            expense.setOutstandingBalance(net.doubleValue());
            creditors.offer(new Balance(expense.getUserName(), expense.getUserIdentity(), net));
        } else if (net.compareTo(BigDecimal.ZERO) < 0) {
            expense.setDueAmount(net.negate().doubleValue());
            debtors.offer(new Balance(expense.getUserName(), expense.getUserIdentity(), net.negate()));
        }

        individualEventExpenseRepository.save(expense);
    }

    private ExpenseSplit createExpenseSplit(Balance debtor, Balance creditor, String eventId, BigDecimal settleAmount) {

        final Optional<ExpenseSplit> expenseSplitOptional
                = expenseSplitRepository.findByEventIdAndSenderUserNameAndReceiverUserName(eventId, debtor.name, creditor.name);

        if(expenseSplitOptional.isPresent())
        {
            throw new InvalidRequestDataException(ResponseMessage.RECORD_ALREADY_EXIST);
        }

        return ExpenseSplit.builder()
                .eventId(eventId)
                .senderUserName(debtor.name)
                .receiverUserName(creditor.name)
                .senderUserIdentity(debtor.userIdentity)
                .receiverUserIdentity(creditor.userIdentity)
                .transactionAmount(settleAmount)
                .currency("BDT")
                .requestStatus(RequestMoneyStatusEnum.PENDING.toString())
                .build();
    }


    public List<ExpenseSplit> fetchEventWiseExpenseSplit(String eventId) {

        final String userIdentity = getUserIdentity();

        return expenseSplitRepository.findAllByEventIdAndSenderUserIdentity(eventId, userIdentity)
                .stream()
                .sorted(Comparator.comparing(ExpenseSplit::getCreatedAt).reversed())
                .toList();


    }

}
