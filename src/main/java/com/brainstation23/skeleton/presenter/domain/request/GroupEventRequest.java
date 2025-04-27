package com.brainstation23.skeleton.presenter.domain.request;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Data
public class GroupEventRequest {
    private String eventId;
    private String groupId;
    private String eventName;
    private String description;
    private Date eventDate;
}
