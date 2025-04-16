package com.brainstation23.skeleton.core.domain.enums;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
public enum OutboundTopicEnum {

    DEFAULT_TOPIC("default"),
    ;
    private String topic;

    OutboundTopicEnum(String topic) {
        this.topic = topic;
    }

    private void setTopic(final String topic) {
        this.topic = topic;
    }

    @Component
    public static class InitializeOutBoundTopic {
        @Value("${bs.kafka.topic.skeleton-test:default}")
        private String defaultTopic;

        @PostConstruct
        public void postConstruct() {
            DEFAULT_TOPIC.setTopic(defaultTopic);
        }
    }

}

