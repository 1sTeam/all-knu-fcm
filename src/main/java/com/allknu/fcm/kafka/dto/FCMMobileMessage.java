package com.allknu.fcm.kafka.dto;

import com.allknu.fcm.core.types.AndroidPriority;
import com.allknu.fcm.core.types.ApnsPriority;
import com.allknu.fcm.core.types.ApnsPushType;
import com.allknu.fcm.core.types.SubscribeType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FCMMobileMessage {
    private List<SubscribeType> subscribeTypes; //보내고자하는 구독 유형 리스트
    private String title;
    private String body;
    private String clickLink;

    private ApnsPushType apnsPushType;
    private ApnsPriority apnsPriority;
    private AndroidPriority androidPriority;
}

