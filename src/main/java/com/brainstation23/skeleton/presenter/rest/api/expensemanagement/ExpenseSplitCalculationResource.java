package com.brainstation23.skeleton.presenter.rest.api.expensemanagement;


import com.brainstation23.skeleton.common.utils.AppUtils;
import com.brainstation23.skeleton.common.utils.ResponseUtils;
import com.brainstation23.skeleton.core.domain.enums.ResponseMessage;
import com.brainstation23.skeleton.core.domain.model.ApiResponse;
import com.brainstation23.skeleton.core.service.ExpenseSplitCalculationService;
import com.brainstation23.skeleton.data.entity.ExpenseSplit;
import com.brainstation23.skeleton.presenter.rest.api.BaseResource;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(AppUtils.BASE_URL + "/expense-calculation")
public class ExpenseSplitCalculationResource extends BaseResource {

    private final ExpenseSplitCalculationService expenseSplitCalculationService;

    @GetMapping("/{eventId}")
    public ApiResponse<List<ExpenseSplit>> splitEventWiseExpense(@PathVariable String eventId) {
        return ResponseUtils.createSuccessResponseObject(getMessage(ResponseMessage.OPERATION_SUCCESSFUL),
                expenseSplitCalculationService.settleExpenses(eventId)
        );
    }
}
