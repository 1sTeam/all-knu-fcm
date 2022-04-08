package com.allknu.fcm.core.types;

import lombok.Getter;

@Getter
public enum AndroidPriority {
    NORMAL("NORMAL"),
    HIGH("HIGH");

    private String value;
    AndroidPriority(String value) {
        this.value = value;
    }
}
