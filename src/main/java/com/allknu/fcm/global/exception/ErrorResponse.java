package com.allknu.fcm.global.exception;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ErrorResponse {

    private String code;
    private String message;
    private int status;
}
