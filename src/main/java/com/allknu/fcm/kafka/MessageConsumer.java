package com.allknu.fcm.kafka;

import com.allknu.fcm.core.types.SubscribeType;
import com.allknu.fcm.kafka.dto.FCMSubscribeMessage;
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
import java.util.Arrays;
import java.util.List;

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
    @KafkaListener(topics = "fcmSubscribe", groupId = "all-knu-fcm", containerFactory = "fcmRequestSubscribeMessageListener")
    public void consume(@Payload FCMSubscribeMessage message, @Headers MessageHeaders headers) throws IOException {
        System.out.println(String.format("Consumed message : %s", message.getToken()));

        try {
            fcmUtil.unsubscribeFromAllTopics(Arrays.asList(message.getToken())); // 구독 전에 기존 모든 구독을 해지한다.

            List<SubscribeType> topics = message.getSubscribes();
            for (SubscribeType topic : topics) {
                fcmUtil.subscribeTopic(Arrays.asList(message.getToken()), topic.toString()); // 반복문으로 토픽 구독
            }
        }catch (FirebaseMessagingException firebaseMessagingException) {
            System.out.println(firebaseMessagingException);
        }
    }
}