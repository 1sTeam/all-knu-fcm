package com.allknu.fcm.kafka;

import com.allknu.fcm.kafka.dto.FCMWebMessage;
import com.allknu.fcm.utils.FCMUtil;
import com.google.firebase.messaging.FirebaseMessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class MessageConsumer {
    private final FCMUtil fcmUtil;

    @KafkaListener(topics = "fcm", groupId = "all-knu-fcm", containerFactory = "fcmRequestMessageListener")
    public void consume(@Payload FCMWebMessage message, @Headers MessageHeaders headers) throws IOException {
        System.out.println(String.format("Consumed message : %s", message.getBody()));

        try {
            fcmUtil.sendFCMToTopics(message);
        }catch (FirebaseMessagingException firebaseMessagingException) {
            System.out.println(firebaseMessagingException);
        }
    }
}