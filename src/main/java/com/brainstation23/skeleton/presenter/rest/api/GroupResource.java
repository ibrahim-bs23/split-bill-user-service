package com.brainstation23.skeleton.presenter.rest.api;

import com.brainstation23.skeleton.common.utils.AppUtils;
import com.brainstation23.skeleton.common.utils.ResponseUtils;
import com.brainstation23.skeleton.core.domain.enums.ResponseMessage;
import com.brainstation23.skeleton.core.domain.model.ApiResponse;
import com.brainstation23.skeleton.core.domain.request.GroupRequest;
import com.brainstation23.skeleton.core.domain.response.GroupResponse;
import com.brainstation23.skeleton.core.service.GroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(AppUtils.BASE_URL + "/group")
@RequiredArgsConstructor
public class GroupResource extends BaseResource {

    private final GroupService groupService;
    @PostMapping("/create")
    public ApiResponse<GroupResponse> createGroup(@RequestBody GroupRequest groupRequest) {
        return ResponseUtils.createSuccessResponseObject(getMessage(ResponseMessage.OPERATION_SUCCESSFUL),
                groupService.createGroup(groupRequest)
        );
    }


}
