package com.brainstation23.skeleton.presenter.rest.external;

import com.brainstation23.skeleton.core.domain.model.ApiResponse;
import com.brainstation23.skeleton.presenter.domain.request.user.PaymentRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


@FeignClient(name = "${services.payment.base-url}", path = "/api/v1/payment", contextId = "payment-service")
public interface PaymentServiceFeignClient {

    @PutMapping("/process/update-status/{transactionId}")
    ApiResponse<?> updateTransactionStatus(@PathVariable String transactionId);
}
