package com.brainstation23.skeleton.presenter.domain.request.devicebinding;

import com.brainstation23.skeleton.core.domain.enums.PlatformType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class DeviceInfoRequest {

    private String pushToken;

    @NotNull
    private PlatformType platformType;

    @NotBlank
    @Schema(defaultValue = "Gp", requiredMode = Schema.RequiredMode.REQUIRED)
    private String platformInfo;

    @NotBlank
    @Schema(defaultValue = "10.0.0", requiredMode = Schema.RequiredMode.REQUIRED)
    private String platformVersion;

    @NotBlank
    @Schema(defaultValue = "74589653694541", requiredMode = Schema.RequiredMode.REQUIRED)
    private String deviceIdentifier;

    @NotBlank
    @Schema(defaultValue = "ENGLISH", requiredMode = Schema.RequiredMode.REQUIRED)
    private String appLanguage;

    @NotBlank
    @Schema(defaultValue = "1.0.0", requiredMode = Schema.RequiredMode.REQUIRED)
    private String appVersion;
}
