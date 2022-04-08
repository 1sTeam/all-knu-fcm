package com.allknu.fcm.core.types;

import lombok.Getter;

@Getter
public enum ApnsPushType {
    ALERT("alert"),
    BACKGROUND("background"),
    LOCATION("location"),
    VOIP("voip"),
    COMPLICATION("complication"),
    FILE_PROVIDER("fileprovider"),
    MDM("mdm");

    private String value;
    ApnsPushType(String value) {
        this.value = value;
    }
}
