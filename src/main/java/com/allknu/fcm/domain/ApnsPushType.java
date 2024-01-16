package com.allknu.fcm.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ApnsPushType {
    ALERT("alert"),
    BACKGROUND("background"),
    LOCATION("location"),
    VOIP("voip"),
    COMPLICATION("complication"),
    FILE_PROVIDER("fileprovider"),
    MDM("mdm");

    private final String value;
}
