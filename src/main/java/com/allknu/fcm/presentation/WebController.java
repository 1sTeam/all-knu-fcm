package com.allknu.fcm.presentation;


import com.allknu.fcm.presentation.dto.CommonResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WebController {

    @GetMapping("/dev/health-check")
    public ResponseEntity<CommonResponse> healthCheck() {
        return new ResponseEntity<>(HttpStatus.OK);
    }
}

