package com.allknu.fcm.utils;

import com.allknu.fcm.kafka.dto.FCMWebMessage;
import com.google.firebase.messaging.*;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;


@Component
public class FCMUtil {
    //토큰들을 주제에 구독하는 메서드
    public TopicManagementResponse subscribeTopic(List<String> tokens, String topic) throws FirebaseMessagingException {
        // These registration tokens come from the client FCM SDKs.

        // Subscribe the devices corresponding to the registration tokens to the
        // topic.
        TopicManagementResponse response = FirebaseMessaging.getInstance().subscribeToTopic(
                tokens, topic);
        // See the TopicManagementResponse reference documentation
        // for the contents of response.
        System.out.println(response.getSuccessCount() + " tokens were subscribed successfully");
        return response;
    }

    // 공식 문서에 보면 토픽 지정 보내기, 여러 토큰에 한번에 보내기가 있다. 필요할 때마다 추가해 사용하자드
    //특정 토큰에 메시지 전송
    public void sendToOneToken(String targetToken, String title, String body, String clickLink) throws FirebaseMessagingException {
        // [START send_to_token]
        // This registration token comes from the client FCM SDKs.
        String registrationToken = targetToken;

        // See documentation on defining a message payload.
        Message message = Message.builder()
                .setToken(registrationToken)
                .setNotification(Notification.builder()
                        .setTitle(title)
                        .setBody(body)
                        .build())
                .setWebpushConfig(WebpushConfig.builder()
                        .setFcmOptions(WebpushFcmOptions.builder()
                                .setLink(clickLink)
                                .build())
                        .build())
                .build();

        // Send a message to the device corresponding to the provided
        // registration token.
        String response = FirebaseMessaging.getInstance().send(message);
        // Response is a message ID string.
        System.out.println("Successfully sent message: " + response);
        // [END send_to_token]
    }

    //토픽들에게 메시지 전송
    public void sendFCMToTopics(FCMWebMessage fcmWebMessage) throws FirebaseMessagingException {
        // Define a condition which will send to devices which are subscribed
        //String condition = "'stock-GOOG' in topics || 'industry-tech' in topics";
        String condition = "";
        if(fcmWebMessage.getSubscribeTypes().size() > 0) {
            condition = "'" + fcmWebMessage.getSubscribeTypes().get(0).toString() + "' in topics";
        }
        for(int i = 1 ; i < fcmWebMessage.getSubscribeTypes().size() ; i++) {
            if(i >= 5) break; // 주제는 5개까지만 된다더라
            condition = condition + " || '" + fcmWebMessage.getSubscribeTypes().get(i).toString() + "' in topics";
        }

        // See documentation on defining a message payload.
        Message message = Message.builder()
                .setNotification(Notification.builder()
                        .setTitle(fcmWebMessage.getTitle())
                        .setBody(fcmWebMessage.getBody())
                        .build())
                .setWebpushConfig(WebpushConfig.builder()
                        .setFcmOptions(WebpushFcmOptions.builder()
                                .setLink(fcmWebMessage.getClickLink())
                                .build())
                        .build())
                .setCondition(condition)
                .build();

        // Send a message to devices subscribed to the combination of topics
        // specified by the provided condition.
        String response = FirebaseMessaging.getInstance().send(message);
        // Response is a message ID string.
        System.out.println("Successfully sent message: " + response);
    }
}
