package com.brainstation23.skeleton.core.service;

import com.brainstation23.skeleton.core.domain.enums.ResponseMessage;
import com.brainstation23.skeleton.core.domain.exceptions.InvalidRequestDataException;
import com.brainstation23.skeleton.core.domain.request.GroupRequest;
import com.brainstation23.skeleton.core.domain.response.GroupResponse;
import com.brainstation23.skeleton.data.entity.Group;
import com.brainstation23.skeleton.data.entity.GroupMember;
import com.brainstation23.skeleton.data.repository.GroupMemberRepository;
import com.brainstation23.skeleton.data.repository.GroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GroupService extends BaseService {

    private final GroupRepository groupRepository;
    private final GroupMemberRepository groupMemberRepository;

    @Transactional
    public GroupResponse createGroup(GroupRequest groupRequest) {

        validateGroupRequest(groupRequest);

        final Group newGroup = createNewGroup(groupRequest);


        Group savedGroup = groupRepository.save(newGroup);

        GroupMember creator = GroupMember.builder()
                .groupId(newGroup.getGroupId())
                .userIdentity("89c47df9-951e-4450-8d65-48ffc5ffad51")
                .role("ADMIN")
                .createdAt(getCurrentDate())
                .build();

        groupMemberRepository.save(creator);

        return mapToGroupResponse(savedGroup);
    }




    public Optional<Group> getGroup(String groupId) {
        if (groupId == null) {
            throw new IllegalArgumentException("Group ID cannot be null");
        }
        return groupRepository.findByGroupId(groupId);
    }

    public List<GroupMember> getGroupMembers(String groupId) {
        if (groupId == null) {
            throw new IllegalArgumentException("Group ID cannot be null");
        }
        return groupMemberRepository.findByGroupId(groupId);
    }

//    @Transactional
//    public GroupMember addMemberToGroup(String groupId, UUID userIdentity, String role) {
//        if (groupId == null || userIdentity == null) {
//            throw new IllegalArgumentException("Group ID and User Identity cannot be null");
//        }
//
//        Optional<Group> group = groupRepository.findByGroupId(groupId);
//        if (group.isPresent()) {
//            // Ensure that the role is either "ADMIN" or "MEMBER"
//            if (!role.equals("ADMIN") && !role.equals("MEMBER")) {
//                throw new IllegalArgumentException("Role must be either 'ADMIN' or 'MEMBER'");
//            }
//
//            GroupMember groupMember = GroupMember.builder()
//                    .groupId(group.get().getGroupId())
//                    .userIdentity(userIdentity)
//                    .role(role)
//                    .build();
//            return groupMemberRepository.save(groupMember);
//        } else {
//            throw new IllegalArgumentException("Group not found");
//        }
//    }

    @Transactional
    public void removeMemberFromGroup(String groupId, UUID userIdentity) {
        if (groupId == null || userIdentity == null) {
            throw new IllegalArgumentException("Group ID and User Identity cannot be null");
        }

        // Ensure that there is always at least one admin before removing an admin
        long adminCount = groupMemberRepository.countByGroupIdAndRole(groupId, "ADMIN");
        if (adminCount <= 1) {
            throw new IllegalArgumentException("Cannot remove the last admin from the group");
        }

        groupMemberRepository.deleteByGroupIdAndUserIdentity(groupId, userIdentity);
    }

//    @Transactional
//    public void deleteGroup(String groupId) {
//        if (groupId == null) {
//            throw new IllegalArgumentException("Group ID cannot be null");
//        }
//
//        groupMemberRepository.deleteByGroupId(groupId);  // Clean up group members first
//        groupRepository.deleteById(groupId);  // Then delete the group itself
//    }

    @Transactional
    public void leaveGroup(String groupId, UUID userIdentity) {
        if (groupId == null || userIdentity == null) {
            throw new IllegalArgumentException("Group ID and User Identity cannot be null");
        }

        // Ensure that there is always at least one admin
        long adminCount = groupMemberRepository.countByGroupIdAndRole(groupId, "ADMIN");
        if (adminCount <= 1 && isAdmin(userIdentity, groupId)) {
            throw new IllegalArgumentException("Cannot leave the group if you are the last admin");
        }

        groupMemberRepository.deleteByGroupIdAndUserIdentity(groupId, userIdentity);
    }

    private Group createNewGroup(GroupRequest groupRequest) {

        final String groupId = UUID.randomUUID().toString().replace("-", "");

        Group group = new Group();
        group.setGroupId(groupId);
        group.setName(groupRequest.getName());
        group.setDescription(groupRequest.getDescription());
        group.setCreatedAt(getCurrentDate());
        group.setCreatedBy("89c47df9-951e-4450-8d65-48ffc5ffad51");

        return group;

    }

    private void validateGroupRequest(GroupRequest groupRequest) {

        if (Objects.isNull(groupRequest) || StringUtils.isEmpty(groupRequest.getName())) {
            throw new InvalidRequestDataException(ResponseMessage.INVALID_REQUEST_DATA);
        }

        Optional<Group> groupOptional = groupRepository.findByName(groupRequest.getName());

        if(groupOptional.isPresent())
        {
            throw new InvalidRequestDataException(ResponseMessage.RECORD_ALREADY_EXIST);
        }

    }

    private GroupResponse mapToGroupResponse(Group savedGroup) {

        return GroupResponse.builder()
                .name(savedGroup.getName())
                .groupId(savedGroup.getGroupId())
                .description(savedGroup.getDescription())
                .isActive(savedGroup.isActive())
                .createdAt(savedGroup.getCreatedAt())
                .build();
    }

    private boolean isAdmin(UUID userIdentity, String groupId) {
//        return groupMemberRepository.findByGroupIdAndUserIdentity(groupId, userIdentity)
//                .map(groupMember -> "ADMIN".equals(groupMember.getRole()))
//                .orElse(false);

        return true;
    }
}
