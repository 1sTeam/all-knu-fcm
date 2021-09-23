package com.allknu.fcm.kafka;

import com.allknu.fcm.kafka.dto.FCMRequestMessage;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class MessageConsumer {

    @KafkaListener(topics = "fcm", groupId = "all-knu-fcm", containerFactory = "fcmRequestMessageListener")
    public void consume(@Payload FCMRequestMessage message, @Headers MessageHeaders headers) throws IOException {
        System.out.println(String.format("Consumed message : %s", message.getBody()));
    }
}