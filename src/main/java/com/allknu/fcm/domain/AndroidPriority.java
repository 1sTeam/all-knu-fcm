package com.allknu.fcm.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AndroidPriority {
    NORMAL("NORMAL"),
    HIGH("HIGH");

    private final String value;
}
