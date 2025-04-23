package com.brainstation23.skeleton.presenter.rest.api.expensemanagement;

import com.brainstation23.skeleton.common.utils.AppUtils;
import com.brainstation23.skeleton.common.utils.ResponseUtils;
import com.brainstation23.skeleton.core.domain.enums.ResponseMessage;
import com.brainstation23.skeleton.core.domain.model.ApiResponse;
import com.brainstation23.skeleton.core.domain.request.ExpenseDetailsRequest;
import com.brainstation23.skeleton.core.domain.request.IndividualExpenseRequest;
import com.brainstation23.skeleton.core.domain.request.UpdateExpenseDetailsRequest;
import com.brainstation23.skeleton.core.domain.request.UpdateIndividualExpense;
import com.brainstation23.skeleton.core.domain.response.ExpenseDetailsResponse;
import com.brainstation23.skeleton.core.domain.response.IndividualExpenseResponse;
import com.brainstation23.skeleton.core.service.BaseService;
import com.brainstation23.skeleton.core.service.ExpenseManagentService;
import com.brainstation23.skeleton.core.service.impl.ExpenseDetailsServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(AppUtils.BASE_URL + "/expense-management")
public class ExpenseManagementController extends BaseService {
    private final ExpenseManagentService expenseManagentService;
    private final ExpenseDetailsServiceImpl expenseDetailsService;

    @PostMapping("/create-event-expense-invoice")
    public ApiResponse<?> createEventExpenseInvoice(@Valid @RequestBody ExpenseDetailsRequest request) {
        expenseDetailsService.createEventExpenseInvoice(request);
        return ResponseUtils.createSuccessResponseObject(getMessage(ResponseMessage.OPERATION_SUCCESSFUL), null);
    }

    @PutMapping("/update-event-expense-invoice")
    public ApiResponse<?> updateEventExpenseInvoice(@Valid @RequestBody UpdateExpenseDetailsRequest request) {
        expenseDetailsService.updateEventExpenseInvoice(request);
        return ResponseUtils.createSuccessResponseObject(getMessage(ResponseMessage.OPERATION_SUCCESSFUL), null);
    }

    @GetMapping("/expense-details/{eventExpenseId}")
    public ApiResponse<?> getExpenseDetailsForUser(@PathVariable Long eventExpenseId) {
        List<ExpenseDetailsResponse> response = expenseDetailsService.getExpenseDetailsForUser(eventExpenseId);
        return ResponseUtils.createSuccessResponseObject(getMessage(ResponseMessage.OPERATION_SUCCESSFUL), response);
    }

    @PutMapping("/update-individual-payment-status")
    public ApiResponse<?> updateIndividualPaymentStatus(@Valid @RequestBody UpdateIndividualExpense request) {
        expenseManagentService.updateIndividualPaymentStatus(request);
        return ResponseUtils.createSuccessResponseObject(getMessage(ResponseMessage.OPERATION_SUCCESSFUL), null);
    }

    @PutMapping("/update-editable-option")
    public ApiResponse<?> updateEditableOption(@Valid @RequestBody UpdateIndividualExpense request) {
        expenseManagentService.updateEditableOption(request);
        return ResponseUtils.createSuccessResponseObject(getMessage(ResponseMessage.OPERATION_SUCCESSFUL), null);
    }

    @GetMapping("/individual-expense/{eventId}")
    public ApiResponse<IndividualExpenseResponse> getIndividualExpense(@PathVariable String eventId) {
        IndividualExpenseResponse response = expenseManagentService.getIndividualExpense(eventId);
        return ResponseUtils.createSuccessResponseObject(getMessage(ResponseMessage.OPERATION_SUCCESSFUL), response);
    }

    @GetMapping("/event-expense-invoices/{eventId}")
    public ApiResponse<List<IndividualExpenseResponse>> getEventExpenseInvoices(@PathVariable String eventId) {
        List<IndividualExpenseResponse> response = expenseManagentService.getEventExpenseInvoices(eventId);
        return ResponseUtils.createSuccessResponseObject(getMessage(ResponseMessage.OPERATION_SUCCESSFUL), response);
    }


}
