package com.brainstation23.skeleton.presenter.rest.api;

import com.brainstation23.skeleton.common.utils.AppUtils;
import com.brainstation23.skeleton.common.utils.ResponseUtils;
import com.brainstation23.skeleton.core.domain.enums.ResponseMessage;
import com.brainstation23.skeleton.core.domain.model.ApiResponse;
import com.brainstation23.skeleton.core.service.GroupEventService;
import com.brainstation23.skeleton.presenter.domain.request.GroupEventRequest;
import com.brainstation23.skeleton.presenter.domain.response.GroupEventResponse;
import com.sun.jdi.request.EventRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(AppUtils.BASE_URL + "/event")
@RequiredArgsConstructor
public class EventResource extends BaseResource {

    private final GroupEventService groupEventService;

    @PostMapping("/createOrUpdate")
    public ApiResponse<GroupEventResponse> createOrUpdateEvent(@RequestBody GroupEventRequest eventRequest) {
        return ResponseUtils.createSuccessResponseObject(getMessage(ResponseMessage.OPERATION_SUCCESSFUL),
                groupEventService.createOrUpdateEvent(eventRequest)
        );
    }
}
