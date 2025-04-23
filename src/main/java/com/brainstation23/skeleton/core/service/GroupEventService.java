package com.brainstation23.skeleton.core.service;

import com.brainstation23.skeleton.core.domain.enums.GroupEventStatusEnum;
import com.brainstation23.skeleton.core.domain.enums.ResponseMessage;
import com.brainstation23.skeleton.core.domain.exceptions.InvalidRequestDataException;
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
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GroupEventService extends BaseService {

    private final GroupRepository groupRepository;

    private final GroupEventRepository groupEventRepository;
    private final GroupMemberRepository groupMemberRepository;

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
        }

        groupEventRepository.save(groupEvent);

        return mapToEventResponse(groupEvent);
    }

    private GroupEvent createGroupEvent(GroupEventRequest eventRequest, GroupMember groupMember) {
        GroupEvent newEvent = new GroupEvent();
        newEvent.setEventId(UUID.randomUUID().toString().replace("-", ""));
        newEvent.setGroupId(eventRequest.getGroupId());
        newEvent.setUserIdentity(groupMember.getUserIdentity());
        newEvent.setEventName(eventRequest.getEventName());
        newEvent.setDescription(eventRequest.getDescription());
        newEvent.setCreatedBy(getUserIdentity());
        newEvent.setCreatedAt(getCurrentDate());
        newEvent.setEventDate(eventRequest.getEventDate());
        newEvent.setTotalSpending(BigDecimal.ZERO);
        newEvent.setEventStatus(GroupEventStatusEnum.ACTIVE.toString());
        return newEvent;
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
