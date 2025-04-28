package com.brainstation23.skeleton.presenter.rest.api;

import com.brainstation23.skeleton.common.utils.AppUtils;
import com.brainstation23.skeleton.common.utils.ResponseUtils;
import com.brainstation23.skeleton.core.domain.enums.ResponseMessage;
import com.brainstation23.skeleton.core.domain.model.ApiResponse;
import com.brainstation23.skeleton.core.service.BaseService;
import com.brainstation23.skeleton.core.service.impl.PaymentService;
import com.brainstation23.skeleton.core.service.impl.UserLinkAccountsService;
import com.brainstation23.skeleton.presenter.domain.request.user.PaymentRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(AppUtils.BASE_URL+"/payment")
public class PaymentController extends BaseService {
    private final PaymentService paymentService;

    @PostMapping("/initiate")
    public ApiResponse<?> initiatePayment(@RequestBody PaymentRequest paymentRequest) {
        paymentService.initiatePaymentProcess(paymentRequest);
        return ResponseUtils.createSuccessResponseObject(getMessage(ResponseMessage.OPERATION_SUCCESSFUL));
    }

}
