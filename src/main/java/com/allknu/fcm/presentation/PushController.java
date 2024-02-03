package com.allknu.fcm.presentation;

import com.allknu.fcm.application.PushService;
import com.allknu.fcm.application.dto.PushToTopicsRequestDto;
import com.allknu.fcm.application.dto.UpdateSubscribeTopicsRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class PushController {

    private final PushService pushService;

    @PostMapping("/api/v1/push/topics")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void pushToTopicsRequestDto(@RequestBody PushToTopicsRequestDto requestDto) {
        pushService.pushToTopics(requestDto);
    }

    @PutMapping("/api/v1/push/topics")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateSubscribeTopics(@RequestBody UpdateSubscribeTopicsRequestDto requestDto) {
        pushService.updateSubscribeTopics(requestDto);
    }
}
