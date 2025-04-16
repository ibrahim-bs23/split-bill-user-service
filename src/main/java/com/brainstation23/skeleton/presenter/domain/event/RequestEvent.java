package com.brainstation23.skeleton.presenter.domain.event;

import lombok.*;

/**
 * Author: Md. Himon Shekh
 * Date: 11/21/2023 12:48 AM
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class RequestEvent {
    private String name;
}