package com.brainstation23.skeleton.common.utils;

import com.brainstation23.skeleton.core.domain.enums.OutboundTopicEnum;

import java.util.Arrays;

/**
 * Author :  Md. Himon Shekh
 * Created at : 14 Dec 2023
 */
public class KafkaTopics {

    public static String[] getTopics() {
        return Arrays.stream(OutboundTopicEnum.values())
                .map(OutboundTopicEnum::getTopic)
                .toArray(String[]::new);
    }
}
