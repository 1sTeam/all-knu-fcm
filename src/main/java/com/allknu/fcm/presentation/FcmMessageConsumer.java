package com.allknu.fcm.presentation;

import com.allknu.fcm.domain.SubscribeType;
import com.allknu.fcm.presentation.dto.FcmMobileMessage;
import com.allknu.fcm.presentation.dto.FcmSubscribeMessage;
import com.allknu.fcm.application.FcmService;
import com.google.firebase.messaging.FirebaseMessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class FcmMessageConsumer {

    private final FcmService fcmService;

    @KafkaListener(topics = "fcm", groupId = "all-knu-fcm", containerFactory = "fcmMobileMessageListener")
    public void consume(@Payload FcmMobileMessage message, @Headers MessageHeaders headers) throws IOException {
        System.out.println(String.format("Consumed message : %s", message.getBody()));

        try {
            fcmService.sendFCMToTopics(message.getSubscribeTypes(),
                    message.getTitle(),
                    message.getBody(),
                    message.getClickLink(),
                    message.getApnsPushType(),
                    message.getApnsPriority(),
                    message.getAndroidPriority());
        }catch (FirebaseMessagingException firebaseMessagingException) {
            System.out.println(firebaseMessagingException);
        }
    }
    @KafkaListener(topics = "fcmSubscribe", groupId = "all-knu-fcm", containerFactory = "fcmRequestSubscribeMessageListener")
    public void consume(@Payload FcmSubscribeMessage message, @Headers MessageHeaders headers) throws IOException {
        System.out.println(String.format("Consumed message : %s", message.getToken()));

        try {
            fcmService.unsubscribeFromAllTopics(Arrays.asList(message.getToken())); // 구독 전에 기존 모든 구독을 해지한다.

            List<SubscribeType> topics = message.getSubscribes();
            if(topics != null) {
                for (SubscribeType topic : topics) {
                    fcmService.subscribeTopic(Arrays.asList(message.getToken()), topic.toString()); // 반복문으로 토픽 구독
                }
            }
        }catch (FirebaseMessagingException firebaseMessagingException) {
            System.out.println(firebaseMessagingException);
        }
    }
}