package com.brainstation23.skeleton.presenter.rest.api;

import com.brainstation23.skeleton.common.utils.AppUtils;
import com.brainstation23.skeleton.common.utils.ResponseUtils;
import com.brainstation23.skeleton.core.domain.enums.ResponseMessage;
import com.brainstation23.skeleton.core.domain.model.ApiResponse;
import com.brainstation23.skeleton.core.domain.request.GroupMemberRequest;
import com.brainstation23.skeleton.core.domain.request.GroupRequest;
import com.brainstation23.skeleton.core.domain.response.GroupResponse;
import com.brainstation23.skeleton.core.service.GroupService;
import com.brainstation23.skeleton.data.entity.Group;
import com.brainstation23.skeleton.data.entity.GroupMember;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(AppUtils.BASE_URL + "/group")
@RequiredArgsConstructor
public class GroupResource extends BaseResource {

    private final GroupService groupService;
    @PostMapping("/createOrUpdate")
    public ApiResponse<GroupResponse> createGroup(@RequestBody GroupRequest groupRequest) {
        return ResponseUtils.createSuccessResponseObject(getMessage(ResponseMessage.OPERATION_SUCCESSFUL),
                groupService.createGroup(groupRequest)
        );
    }

    @GetMapping("/all")
    public ApiResponse<List<Group>> fetchUserWiseGroups() {
        return ResponseUtils.createSuccessResponseObject(getMessage(ResponseMessage.OPERATION_SUCCESSFUL),
                groupService.fetchUserWiseGroups()
        );
    }

    @GetMapping("/all-members/{groupId}")
    public ApiResponse<List<GroupMember>> fetchUserWiseGroupWithMembers(@PathVariable String groupId) {
        return ResponseUtils.createSuccessResponseObject(getMessage(ResponseMessage.OPERATION_SUCCESSFUL),
                groupService.fetchUserWiseGroupWithMembers(groupId)
        );
    }

    @PostMapping("/add-members")
    public ApiResponse<List<GroupMember>> addMemberToGroup(@RequestBody GroupMemberRequest groupMemberRequest) {
        return ResponseUtils.createSuccessResponseObject(getMessage(ResponseMessage.OPERATION_SUCCESSFUL),
                groupService.addMemberToGroup(groupMemberRequest)
        );
    }

    @PostMapping("/make-admin")
    public ApiResponse<List<GroupMember>> makeUserAdmin(@RequestBody GroupMemberRequest groupMemberRequest) {
        return ResponseUtils.createSuccessResponseObject(getMessage(ResponseMessage.OPERATION_SUCCESSFUL),
                groupService.makeUserAdmin(groupMemberRequest)
        );
    }

    @PostMapping("/make-member")
    public ApiResponse<List<GroupMember>> makeUserMember(@RequestBody GroupMemberRequest groupMemberRequest) {
        return ResponseUtils.createSuccessResponseObject(getMessage(ResponseMessage.OPERATION_SUCCESSFUL),
                groupService.makeUserMember(groupMemberRequest)
        );
    }

    @DeleteMapping("/remove-member")
    public ApiResponse<List<GroupMember>> removeMember(@RequestBody GroupMemberRequest groupMemberRequest) {
        return ResponseUtils.createSuccessResponseObject(getMessage(ResponseMessage.OPERATION_SUCCESSFUL),
                groupService.removeMemberFromGroup(groupMemberRequest)
        );
    }

    @PostMapping("/leave-group/{groupId}")
    public ApiResponse<String> leaveGroup(@PathVariable String groupId) {
        return ResponseUtils.createSuccessResponseObject(getMessage(ResponseMessage.OPERATION_SUCCESSFUL),
                groupService.leaveGroup(groupId)
        );
    }

    @DeleteMapping("/remove-group/{groupId}")
    public ApiResponse<String> removeGroup(@PathVariable String groupId) {
        return ResponseUtils.createSuccessResponseObject(getMessage(ResponseMessage.OPERATION_SUCCESSFUL),
                groupService.deleteGroup(groupId)
        );
    }

}
