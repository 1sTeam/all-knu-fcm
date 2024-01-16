package com.allknu.fcm.presentation.dto;

import com.allknu.fcm.domain.SubscribeType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FcmWebMessage {

    private List<SubscribeType> subscribeTypes; //보내고자하는 구독 유형 리스트
    private String title;
    private String body;
    private String clickLink;
}
