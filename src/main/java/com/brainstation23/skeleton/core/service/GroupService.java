package com.brainstation23.skeleton.core.service;

import com.brainstation23.skeleton.core.domain.enums.GroupMemberTypeEnum;
import com.brainstation23.skeleton.core.domain.enums.ResponseMessage;
import com.brainstation23.skeleton.core.domain.exceptions.InvalidRequestDataException;
import com.brainstation23.skeleton.core.domain.request.GroupMemberRequest;
import com.brainstation23.skeleton.core.domain.request.GroupRequest;
import com.brainstation23.skeleton.core.domain.response.GroupResponse;
import com.brainstation23.skeleton.data.entity.Group;
import com.brainstation23.skeleton.data.entity.GroupMember;
import com.brainstation23.skeleton.data.entity.user.Users;
import com.brainstation23.skeleton.data.repository.GroupMemberRepository;
import com.brainstation23.skeleton.data.repository.GroupRepository;
import com.brainstation23.skeleton.data.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GroupService extends BaseService {

    private final GroupRepository groupRepository;
    private final GroupMemberRepository groupMemberRepository;
    private final UserRepository userRepository;

    @Transactional
    public GroupResponse createGroup(GroupRequest groupRequest) {

        validateGroupRequest(groupRequest);

        Group group = groupRepository.findByGroupId(groupRequest.getGroupId())
                .orElseGet(() -> createNewGroup(groupRequest));

        group.setName(groupRequest.getName());
        group.setDescription(groupRequest.getDescription());
        group.setUpdatedAt(getCurrentDate());

        Group savedGroup = groupRepository.save(group);

        List<GroupMember> groupMemberList = groupMemberRepository.findByGroupId(group.getGroupId());


        if (groupMemberList.isEmpty()) {
            final Optional<Users> userOptional = userRepository.findByUserIdentity(getUserIdentity());
            if (userOptional.isPresent()) {
                GroupMember creator = GroupMember.builder()
                        .groupId(savedGroup.getGroupId())
                        .userIdentity(userOptional.get().getUserIdentity())
                        .role(GroupMemberTypeEnum.ADMIN.toString())
                        .createdAt(getCurrentDate())
                        .userName(userOptional.get().getUsername())
                        .build();
                groupMemberRepository.save(creator);
            }

        }

        return mapToGroupResponse(savedGroup);
    }

    public List<Group> fetchUserWiseGroups() {
        List<GroupMember> groupMemberList
                = groupMemberRepository.findAllByUserIdentity(getUserIdentity());

        List<String> groupIds = groupMemberList.stream()
                .map(GroupMember::getGroupId)
                .collect(Collectors.toList());

        List<Group> userGroups = groupRepository.findAllByGroupIdIn(groupIds);

        return userGroups;
    }

    public List<GroupMember> fetchUserWiseGroupWithMembers(String groupId) {

        List<GroupMember> groupMemberList
                = groupMemberRepository.findAllByGroupId(groupId);

        final boolean isUserFound = groupMemberList.stream()
                .anyMatch(member -> member.getUserIdentity().equals(getUserIdentity()));

        if (isUserFound) {
            return groupMemberList;
        } else {
            throw new InvalidRequestDataException(ResponseMessage.UNAUTHORIZED_RESOURCE_ACCESS);
        }
    }

    @Transactional
    public List<GroupMember> addMemberToGroup(GroupMemberRequest groupMemberRequest) {

        validateGroupMemberRequest(groupMemberRequest);

        Group group = groupRepository.findByGroupId(groupMemberRequest.getGroupId())
                .orElseThrow(() -> new InvalidRequestDataException(ResponseMessage.RECORD_NOT_FOUND));


        List<GroupMember> groupMembers = groupMemberRepository.findByGroupId(group.getGroupId());

        boolean isEligibleUser = groupMembers.stream()
                .anyMatch(groupMember -> groupMember.getUserIdentity().equals(getUserIdentity())
                        && groupMember.getRole().equals(GroupMemberTypeEnum.ADMIN.toString()));

        if (!isEligibleUser) {
            throw new InvalidRequestDataException(ResponseMessage.UNAUTHORIZED_RESOURCE_ACCESS);
        }

        for (String userName : groupMemberRequest.getUsernames()) {
            userRepository.findByUsername(userName)
                    .ifPresent(user -> addNewMember(userName, user.getUserIdentity(), groupMembers));
        }

        groupMemberRepository.saveAll(groupMembers);

        return groupMembers;

    }

    private void addNewMember(String username, String memberIdentity, List<GroupMember> groupMemberList) {

        if (memberIdentity.equals(getUserIdentity()) ||
                groupMemberList.stream().anyMatch(member -> member.getUserName().equals(username))) {
            throw new InvalidRequestDataException(ResponseMessage.RECORD_ALREADY_EXIST);
        }

        GroupMember groupMember = GroupMember.builder()
                .groupId(groupMemberList.get(0).getGroupId())
                .userName(username)
                .userIdentity(memberIdentity)
                .role(GroupMemberTypeEnum.MEMBER.toString())
                .createdAt(getCurrentDate())
                .build();

        groupMemberList.add(groupMember);
    }

    private void validateGroupMemberRequest(GroupMemberRequest groupMemberRequest) {

        if (Objects.isNull(groupMemberRequest) || Objects.isNull(groupMemberRequest.getUsernames())
                || Objects.isNull(groupMemberRequest.getGroupId())) {
            throw new InvalidRequestDataException(ResponseMessage.INVALID_REQUEST_DATA);
        }

    }

    @Transactional
    public List<GroupMember> makeUserAdmin(GroupMemberRequest groupMemberRequest) {

        validateGroupMemberRequest(groupMemberRequest);

        final String currentUserIdentity = getUserIdentity();

        List<GroupMember> groupMembers = groupMemberRepository.findByGroupId(groupMemberRequest.getGroupId());

        groupMembers.stream()
                .filter(member -> member.getUserIdentity().equals(currentUserIdentity)
                        && GroupMemberTypeEnum.ADMIN.toString().equals(member.getRole())
                )
                .findFirst()
                .orElseThrow(() -> new InvalidRequestDataException(ResponseMessage.UNAUTHORIZED_RESOURCE_ACCESS));

        for (String userName : groupMemberRequest.getUsernames()) {
            promoteToAdmin(userName, groupMembers);
        }

        return groupMemberRepository.saveAll(groupMembers);
    }

    private static GroupMember promoteToAdmin(String username, List<GroupMember> groupMembers) {

        GroupMember groupMember = groupMembers.stream()
                .filter(member -> member.getUserName().equals(username))
                .findFirst()
                .orElseThrow(() -> new InvalidRequestDataException(ResponseMessage.USER_NOT_FOUND));

        if (GroupMemberTypeEnum.ADMIN.toString().equals(groupMember.getRole())) {
            throw new InvalidRequestDataException(ResponseMessage.USER_ALREADY_ADMIN);
        }

        groupMember.setRole(GroupMemberTypeEnum.ADMIN.toString());
        return groupMember;
    }


    @Transactional
    public List<GroupMember> makeUserMember(GroupMemberRequest groupMemberRequest) {

        validateGroupMemberRequest(groupMemberRequest);

        String currentUserIdentity = getUserIdentity();

        List<GroupMember> groupMembers = groupMemberRepository.findByGroupId(groupMemberRequest.getGroupId());

        groupMembers.stream()
                .filter(member -> member.getUserIdentity().equals(currentUserIdentity)
                        && GroupMemberTypeEnum.ADMIN.toString().equals(member.getRole())
                )
                .findFirst()
                .orElseThrow(() -> new InvalidRequestDataException(ResponseMessage.UNAUTHORIZED_RESOURCE_ACCESS));


        for (String userName : groupMemberRequest.getUsernames()) {
            demoteToMember(userName, groupMembers);
        }

        return groupMemberRepository.saveAll(groupMembers);

    }

    private static GroupMember demoteToMember(String username, List<GroupMember> groupMembers) {

        GroupMember groupMember = groupMembers.stream()
                .filter(member -> member.getUserName().equals(username) &&
                        GroupMemberTypeEnum.ADMIN.toString().equals(member.getRole()))
                .findFirst()
                .orElseThrow(() -> new InvalidRequestDataException(ResponseMessage.USER_NOT_FOUND));

        long adminCount = groupMembers.stream().filter(member -> GroupMemberTypeEnum.ADMIN.toString().equals(member.getRole())).count();
        if (adminCount <= 1) {
            throw new InvalidRequestDataException(ResponseMessage.MINIMUM_ONE_ADMIN_REQUIRED);
        }

        groupMember.setRole(GroupMemberTypeEnum.MEMBER.toString());
        return groupMember;
    }


    @Transactional
    public List<GroupMember> removeMemberFromGroup(GroupMemberRequest groupMemberRequest) {

        validateGroupMemberRequest(groupMemberRequest);

        List<GroupMember> groupMembers = groupMemberRepository.findByGroupId(groupMemberRequest.getGroupId());

        String currentUserIdentity = getUserIdentity();

        groupMembers.stream()
                .filter(member -> member.getUserIdentity().equals(currentUserIdentity)
                        && GroupMemberTypeEnum.ADMIN.toString().equals(member.getRole())
                )
                .findFirst()
                .orElseThrow(() -> new InvalidRequestDataException(ResponseMessage.UNAUTHORIZED_RESOURCE_ACCESS));

        for (String userName : groupMemberRequest.getUsernames()) {
            removeMember(userName, groupMembers);
        }

        return groupMemberRepository.saveAll(groupMembers);

    }

    private void removeMember(String userName, List<GroupMember> groupMembers) {

        GroupMember groupMember = groupMembers.stream()
                .filter(member -> member.getUserName().equals(userName))
                .findFirst()
                .orElseThrow(() -> new InvalidRequestDataException(ResponseMessage.USER_NOT_FOUND));

        if (groupMember.getRole().equals(GroupMemberTypeEnum.ADMIN.toString())) {
            long adminCount
                    = groupMembers.stream()
                    .filter(member -> GroupMemberTypeEnum.ADMIN.toString()
                            .equals(member.getRole())).count() - 1;
            if (adminCount < 1) {
                throw new InvalidRequestDataException(ResponseMessage.MINIMUM_ONE_ADMIN_REQUIRED);
            }
        }

        groupMemberRepository.delete(groupMember);
        groupMembers.remove(groupMember);

    }

    @Transactional
    public String deleteGroup(String groupId) {

        List<GroupMember> groupMembers = groupMemberRepository.findByGroupId(groupId);

        String currentUserIdentity = "89c47df9-951e-4450-8d65-48ffc5ffad51";

        groupMembers.stream()
                .filter(member -> member.getUserIdentity().equals(currentUserIdentity)
                        && GroupMemberTypeEnum.ADMIN.toString().equals(member.getRole())
                )
                .findFirst()
                .orElseThrow(() -> new InvalidRequestDataException(ResponseMessage.UNAUTHORIZED_RESOURCE_ACCESS));

        groupMemberRepository.deleteAllByGroupId(groupId);

        final Group group = groupRepository.findByGroupId(groupId)
                .orElseThrow(() -> new InvalidRequestDataException(ResponseMessage.RECORD_NOT_FOUND));

        group.setActive(false);

        groupRepository.save(group);

        return ResponseMessage.OPERATION_SUCCESSFUL.getResponseMessage();
    }

    @Transactional
    public String leaveGroup(String groupId) {

        String userIdentity = getUserIdentity();

        final GroupMember groupMember = groupMemberRepository.findByGroupIdAndUserIdentity(groupId, userIdentity)
                .orElseThrow(() -> new InvalidRequestDataException(ResponseMessage.USER_NOT_FOUND));

        long adminCount = groupMemberRepository.countByGroupIdAndRole(groupId, GroupMemberTypeEnum.ADMIN.toString());
        if (adminCount <= 1 && isAdmin(userIdentity, groupId)) {
            throw new InvalidRequestDataException(ResponseMessage.MINIMUM_ONE_ADMIN_REQUIRED);
        }

        groupMemberRepository.delete(groupMember);

        return ResponseMessage.OPERATION_SUCCESSFUL.getResponseMessage();
    }

    private Group createNewGroup(GroupRequest groupRequest) {

        final String groupId = UUID.randomUUID().toString().replace("-", "");

        Group group = new Group();
        group.setGroupId(groupId);
        group.setName(groupRequest.getName());
        group.setDescription(groupRequest.getDescription());
        group.setCreatedAt(getCurrentDate());
        group.setCreatedBy(getUserIdentity());

        return group;

    }

    private void validateGroupRequest(GroupRequest groupRequest) {

        if (Objects.isNull(groupRequest) || StringUtils.isEmpty(groupRequest.getName())) {
            throw new InvalidRequestDataException(ResponseMessage.INVALID_REQUEST_DATA);
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

    private boolean isAdmin(String userIdentity, String groupId) {

        return groupMemberRepository.findByGroupIdAndUserIdentity(groupId, userIdentity)
                .map(groupMember -> GroupMemberTypeEnum.ADMIN.toString().equals(groupMember.getRole()))
                .orElse(false);
    }
}
