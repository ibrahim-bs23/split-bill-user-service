package com.brainstation23.skeleton.presenter.domain.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Builder
public class GroupEventResponse {
    private Long eventId;
    private String eventName;
    private Date eventDate;
    private BigDecimal totalSpending;
    private String status;
}
