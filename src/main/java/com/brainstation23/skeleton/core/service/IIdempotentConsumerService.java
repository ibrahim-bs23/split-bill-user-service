package com.brainstation23.skeleton.core.service;


import com.brainstation23.kafka.domain.EventWrapper;
import com.brainstation23.skeleton.presenter.domain.event.RequestEvent;

public interface IIdempotentConsumerService {

    void processRequest(EventWrapper<RequestEvent> eventWrapper);
}
