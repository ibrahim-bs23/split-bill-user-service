package com.brainstation23.skeleton.presenter.rest.external;

import org.springframework.cloud.openfeign.FeignClient;

/**
 * Author: Md. Himon Shekh
 * Date: 12/14/2023 7:05 PM
 */


@FeignClient(name = "${services.remittance.base-url}", path = "/api/v1/remittance", contextId = "remittance-service")
public interface SkeletonServiceFeign {

}
