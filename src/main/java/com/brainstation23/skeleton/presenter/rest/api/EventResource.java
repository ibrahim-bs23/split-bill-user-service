package com.brainstation23.skeleton.presenter.rest.api;

import com.brainstation23.skeleton.common.utils.AppUtils;
import com.brainstation23.skeleton.common.utils.ResponseUtils;
import com.brainstation23.skeleton.core.domain.enums.ResponseMessage;
import com.brainstation23.skeleton.core.domain.model.ApiResponse;
import com.brainstation23.skeleton.core.service.GroupEventService;
import com.brainstation23.skeleton.data.entity.GroupEvent;
import com.brainstation23.skeleton.presenter.domain.request.GroupEventRequest;
import com.brainstation23.skeleton.presenter.domain.response.GroupEventResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @PostMapping("/{eventId}/add-members")
    public ApiResponse<String> addParticipantsToEvent(@PathVariable String eventId, @RequestBody List<String> usernames) {
        return ResponseUtils.createSuccessResponseObject(getMessage(ResponseMessage.OPERATION_SUCCESSFUL),
                groupEventService.addMemberToEvent(eventId, usernames)
        );
    }

    @PostMapping("/{eventId}/remove-members")
    public ApiResponse<GroupEvent> removeParticipantsFromEvent(@PathVariable String eventId, @RequestBody List<String> usernames) {
        return ResponseUtils.createSuccessResponseObject(getMessage(ResponseMessage.OPERATION_SUCCESSFUL),
                groupEventService.removeMembersFromEvent(eventId, usernames)
        );
    }

    @GetMapping("/{eventId}/all-members")
    public ApiResponse<List<String>> fetchAllMembersFromEvent(@PathVariable String eventId) {
        return ResponseUtils.createSuccessResponseObject(getMessage(ResponseMessage.OPERATION_SUCCESSFUL),
                groupEventService.fetchMembersFromEvent(eventId)
        );
    }

    @DeleteMapping("/{eventId}")
    public ApiResponse<String> deleteEvent(@PathVariable String eventId) {
        return ResponseUtils.createSuccessResponseObject(getMessage(ResponseMessage.OPERATION_SUCCESSFUL),
                groupEventService.deleteEvent(eventId)
        );
    }

    @GetMapping("/events/{groupId}")
    public ApiResponse<List<GroupEventResponse>> fetchUserWiseEvents(@PathVariable String groupId) {
        return ResponseUtils.createSuccessResponseObject(getMessage(ResponseMessage.OPERATION_SUCCESSFUL),
                groupEventService.fetchUserWiseEvents(groupId)
        );
    }

    @GetMapping("/{eventId}")
    public ApiResponse<GroupEventResponse> fetchUserWiseEventDetails(@PathVariable String eventId) {
        return ResponseUtils.createSuccessResponseObject(getMessage(ResponseMessage.OPERATION_SUCCESSFUL),
                groupEventService.fetchUserWiseEventDetails(eventId)
        );
    }

}
