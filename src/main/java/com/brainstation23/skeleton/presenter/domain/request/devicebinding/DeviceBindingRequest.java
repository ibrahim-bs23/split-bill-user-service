package com.brainstation23.skeleton.presenter.domain.request.devicebinding;

import com.brainstation23.skeleton.presenter.domain.request.TfaVerificationRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class DeviceBindingRequest extends TfaVerificationRequest {

    private DeviceInfoRequest deviceInfo;
}
