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
public class FcmSubscribeMessage {

    private String token;
    private List<SubscribeType> subscribes;
}
