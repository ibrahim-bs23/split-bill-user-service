package com.brainstation23.skeleton.presenter.service.nonTransactional;

import com.brainstation23.skeleton.core.domain.enums.ApiResponseCode;
import com.brainstation23.skeleton.core.domain.exceptions.InterServiceCommunicationException;
import com.brainstation23.skeleton.core.domain.model.ApiResponse;
import com.brainstation23.skeleton.core.service.BaseService;
import com.brainstation23.skeleton.presenter.domain.request.user.PaymentRequest;
import com.brainstation23.skeleton.presenter.rest.external.PaymentServiceFeignClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class NonTransactionalIntegrationService extends BaseService {

  private final PaymentServiceFeignClient paymentServiceFeignClient;

  public void updatePaymentHistory(String transactionId) {

      ApiResponse<?> response = paymentServiceFeignClient.updateTransactionStatus(transactionId);
      if (ApiResponseCode.isNotOperationSuccessful(response)
              || Objects.isNull(response.getData())) {
          logger.trace("Error processing payment: " + writeJsonString(request));
          throw new InterServiceCommunicationException(response.getResponseMessage());
      }
  }
}
