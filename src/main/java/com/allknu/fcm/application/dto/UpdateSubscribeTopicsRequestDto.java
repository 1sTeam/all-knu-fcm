package com.allknu.fcm.application.dto;

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
public class UpdateSubscribeTopicsRequestDto {

    private String token;
    private List<SubscribeType> subscribes;
}
