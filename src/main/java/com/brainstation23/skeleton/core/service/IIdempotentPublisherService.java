package com.brainstation23.skeleton.core.service;

public interface IIdempotentPublisherService {
    void processOutboxEvent(Object eventRequest, String topic);
}
