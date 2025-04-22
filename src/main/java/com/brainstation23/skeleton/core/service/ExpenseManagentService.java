package com.brainstation23.skeleton.core.service;

import com.brainstation23.skeleton.core.domain.request.ExpenseDetailsRequest;
import com.brainstation23.skeleton.core.domain.request.IndividualExpenseRequest;
import com.brainstation23.skeleton.core.domain.request.UpdateExpenseRequest;
import com.brainstation23.skeleton.data.entity.expense.EventExpenseInvoice;

public interface ExpenseManagentService {

    void createIndividualExpense(IndividualExpenseRequest request);

    void updateIndividualExpense(UpdateExpenseRequest request);

}
