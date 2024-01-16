package com.allknu.fcm.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ApnsPriority {
    FIVE("5"),
    TEN("10");

    private final String value;
}
