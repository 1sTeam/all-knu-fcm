package com.allknu.fcm.kafka.dto;

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
public class FCMSubscribeMessage {
    private String token;
    private List<SubscribeType> subscribes;
}
