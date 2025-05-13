package com.brainstation23.skeleton.core.service;

import com.brainstation23.skeleton.core.domain.enums.GroupEventStatusEnum;
import com.brainstation23.skeleton.core.domain.enums.ResponseMessage;
import com.brainstation23.skeleton.core.domain.exceptions.InvalidRequestDataException;
import com.brainstation23.skeleton.core.domain.exceptions.RecordNotFoundException;
import com.brainstation23.skeleton.core.domain.request.IndividualExpenseRequest;
import com.brainstation23.skeleton.data.entity.GroupEvent;
import com.brainstation23.skeleton.data.entity.GroupMember;
import com.brainstation23.skeleton.data.repository.GroupEventRepository;
import com.brainstation23.skeleton.data.repository.GroupMemberRepository;
import com.brainstation23.skeleton.data.repository.GroupRepository;
import com.brainstation23.skeleton.presenter.domain.request.GroupEventRequest;
import com.brainstation23.skeleton.presenter.domain.response.GroupEventResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GroupEventService extends BaseService {

    private final GroupRepository groupRepository;

    private final GroupEventRepository groupEventRepository;
    private final GroupMemberRepository groupMemberRepository;
    private final ExpenseManagentService expenseManagentService;

    @Transactional
    public GroupEventResponse createOrUpdateEvent(GroupEventRequest eventRequest) {

        validateEventRequest(eventRequest);

        final String userIdentity = getUserIdentity();

        GroupMember groupMember = groupMemberRepository.findByGroupIdAndUserIdentity(
                eventRequest.getGroupId(),
                userIdentity
        ).orElseThrow(() -> new InvalidRequestDataException(ResponseMessage.GROUP_NOT_FOUND));


        GroupEvent groupEvent;

        if (Objects.nonNull(eventRequest.getEventId())) {

            groupEvent = groupEventRepository.findFirstByEventId(eventRequest.getEventId())
                    .orElseThrow(() -> new InvalidRequestDataException(ResponseMessage.RECORD_NOT_FOUND));
            groupEvent.setEventName(eventRequest.getEventName());
            groupEvent.setDescription(eventRequest.getDescription());
            groupEvent.setEventDate(eventRequest.getEventDate());

        } else {
            groupEvent = createGroupEvent(eventRequest, groupMember);
            expenseManagentService.createIndividualExpense(buildIndividualExpenseRequest(groupEvent));
        }

        groupEventRepository.save(groupEvent);

        return mapToEventResponse(groupEvent);
    }

    private IndividualExpenseRequest buildIndividualExpenseRequest(GroupEvent groupEvent) {
        return IndividualExpenseRequest.builder()
                .eventId(groupEvent.getEventId())
                .groupId(groupEvent.getGroupId())
                .userIdentity(groupEvent.getUserIdentity())
                .userName(groupEvent.getUsername())
                .build();
    }

    private GroupEvent createGroupEvent(GroupEventRequest eventRequest, GroupMember groupMember) {
        GroupEvent newEvent = new GroupEvent();
        newEvent.setEventId(UUID.randomUUID().toString().replace("-", ""));
        newEvent.setGroupId(eventRequest.getGroupId());
        newEvent.setUserIdentity(groupMember.getUserIdentity());
        newEvent.setUsername(groupMember.getUserName());
        newEvent.setEventName(eventRequest.getEventName());
        newEvent.setDescription(eventRequest.getDescription());
        newEvent.setCreatedBy(groupMember.getUserIdentity());
        newEvent.setCreatedAt(getCurrentDate());
        newEvent.setEventDate(eventRequest.getEventDate());
        newEvent.setTotalSpending(BigDecimal.ZERO);
        newEvent.setEventStatus(GroupEventStatusEnum.ACTIVE.toString());
        return newEvent;
    }

    @Transactional
    public String addMemberToEvent(String eventId, List<String> usernames) {

        GroupEvent groupEvent = groupEventRepository.findFirstByEventId(eventId)
                .orElseThrow(() -> new InvalidRequestDataException(ResponseMessage.EVENT_NOT_FOUND));

        String currentUserIdentity = getUserIdentity();

        validateEventUser(groupEvent.getEventId(), currentUserIdentity);

        for (String username : usernames) {
            addUserToEvent(groupEvent, username);
        }

        return ResponseMessage.OPERATION_SUCCESSFUL.getResponseMessage();
    }

    private void addUserToEvent(GroupEvent groupEvent, String username) {

        GroupMember groupMember = groupMemberRepository.findByGroupIdAndUserName(groupEvent.getGroupId(), username)
                .orElseThrow(() -> new InvalidRequestDataException(ResponseMessage.GROUP_NOT_FOUND));

        Optional<GroupEvent> groupEventOptional
                = groupEventRepository.findByEventIdAndUsername(groupEvent.getEventId(), username);

        if(groupEventOptional.isPresent())
        {
            throw new InvalidRequestDataException(ResponseMessage.USER_NAME_ALREADY_EXISTS);
        }

        GroupEvent participantEvent = GroupEvent.builder()
                .eventId(groupEvent.getEventId())
                .groupId(groupEvent.getGroupId())
                .userIdentity(groupMember.getUserIdentity())
                .eventName(groupEvent.getEventName())
                .description(groupEvent.getDescription())
                .createdBy(groupEvent.getCreatedBy())
                .createdAt(getCurrentDate())
                .eventDate(groupEvent.getEventDate())
                .totalSpending(groupEvent.getTotalSpending())
                .eventStatus(GroupEventStatusEnum.ACTIVE.toString())
                .username(username)
                .build();

        expenseManagentService.createIndividualExpense(buildIndividualExpenseRequest(participantEvent));
        groupEventRepository.save(participantEvent);

    }

    @Transactional
    public GroupEvent removeMembersFromEvent(String eventId, List<String> usernames) {

        GroupEvent groupEvent = groupEventRepository.findFirstByEventId(eventId)
                .orElseThrow(() -> new InvalidRequestDataException(ResponseMessage.EVENT_NOT_FOUND));

        String currentUserIdentity = getUserIdentity();

        validateEventUser(groupEvent.getEventId(), currentUserIdentity);

        for (String username : usernames) {
            removeParticipantFromEvent(groupEvent, username);
        }

        return groupEvent;

    }

    @Transactional
    public void removeParticipantFromEvent(GroupEvent event, String username) {

        GroupEvent participantEvent = groupEventRepository.findByEventIdAndUsername(event.getEventId(), username)
                .orElseThrow(() -> new InvalidRequestDataException(ResponseMessage.EVENT_NOT_FOUND));

        groupEventRepository.delete(participantEvent);
        deleteIndividualExpense(participantEvent);
    }

    @Transactional
    public void deleteIndividualExpense(GroupEvent participantEvent) {
        expenseManagentService.deleteIndividualExpense(participantEvent.getEventId(), participantEvent.getUsername());
    }

    @Transactional
    public String deleteEvent(String eventId) {

        GroupEvent groupEvent = groupEventRepository.findFirstByEventId(eventId)
                .orElseThrow(() -> new InvalidRequestDataException(ResponseMessage.EVENT_NOT_FOUND));

        String currentUserIdentity = getUserIdentity();

        validateEventUser(groupEvent.getGroupId(), currentUserIdentity);

        groupEventRepository.deleteAllByEventId(eventId);

        return ResponseMessage.OPERATION_SUCCESSFUL.getResponseMessage();

    }


    @Transactional
    public List<GroupEventResponse> fetchUserWiseEvents() {

        String currentUserIdentity = getUserIdentity();

        List<GroupEvent> groupEventList = groupEventRepository.findAllByUserIdentity(currentUserIdentity);

        List<GroupEventResponse> groupEventResponseList = groupEventList.stream()
                .map(groupEvent -> mapToEventResponse(groupEvent))
                .collect(Collectors.toList());

        return groupEventResponseList;
    }

    @Transactional
    public GroupEventResponse fetchUserWiseEventDetails(String eventId) {

        String currentUserIdentity = getUserIdentity();

        validateEventUser(eventId, currentUserIdentity);

        GroupEvent groupEvent = groupEventRepository.findFirstByEventId(eventId)
                .orElseThrow(() -> new InvalidRequestDataException(ResponseMessage.EVENT_NOT_FOUND));

        return mapToEventResponse(groupEvent);
    }


    private void validateEventUser(String eventId, String userIdentity) {

        groupEventRepository.findByEventIdAndUserIdentity(eventId, userIdentity)
                .orElseThrow(() -> new InvalidRequestDataException(ResponseMessage.USER_NOT_FOUND));

    }


    private void validateEventRequest(GroupEventRequest eventRequest) {
        if (Objects.isNull(eventRequest.getGroupId()) || eventRequest.getEventName().isEmpty()) {
            throw new InvalidRequestDataException(ResponseMessage.INVALID_REQUEST_DATA);
        }
    }

    private GroupEventResponse mapToEventResponse(GroupEvent event) {
        return GroupEventResponse.builder()
                .eventId(event.getId())
                .eventName(event.getEventName())
                .eventDate(event.getEventDate())
                .totalSpending(event.getTotalSpending())
                .status(event.getEventStatus())
                .build();
    }
}
