package com.brainstation23.skeleton.core.service;

import com.brainstation23.skeleton.core.domain.request.ExpenseDetailsRequest;
import com.brainstation23.skeleton.core.domain.request.IndividualExpenseRequest;
import com.brainstation23.skeleton.core.domain.request.UpdateExpenseRequest;
import com.brainstation23.skeleton.core.domain.request.UpdateIndividualExpense;
import com.brainstation23.skeleton.core.domain.response.IndividualExpenseResponse;
import com.brainstation23.skeleton.data.entity.expense.EventExpenseInvoice;

import java.util.List;

public interface ExpenseManagentService {

    void createIndividualExpense(IndividualExpenseRequest request);

    void updateIndividualExpense(UpdateExpenseRequest request);

    void updateIndividualPaymentStatus(UpdateIndividualExpense request);

    void updateEditableOption(UpdateIndividualExpense request);

    IndividualExpenseResponse getIndividualExpense(String eventId);

    List<IndividualExpenseResponse> getEventExpenseInvoices(String eventId);

}
