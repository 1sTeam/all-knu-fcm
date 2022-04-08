package com.allknu.fcm.utils;

import com.allknu.fcm.core.types.AndroidPriority;
import com.allknu.fcm.core.types.ApnsPriority;
import com.allknu.fcm.core.types.ApnsPushType;
import com.allknu.fcm.core.types.SubscribeType;
import com.allknu.fcm.kafka.dto.FCMWebMessage;
import com.google.firebase.messaging.*;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.EnumSet;
import java.util.List;


@Component
public class FCMUtil {
    //unsubscribe from all topic
    public void unsubscribeFromAllTopics(List<String> tokens) {
        //해당 토큰들의 모든 구독을 취소한다.
        EnumSet.allOf(SubscribeType.class)
                .forEach(type -> {
                    try {
                        TopicManagementResponse response = FirebaseMessaging.getInstance().unsubscribeFromTopic(tokens, type.toString());
                        System.out.println(response.getSuccessCount() + " tokens were unsubscribed successfully");
                    } catch (FirebaseMessagingException e) {
                        System.out.println("구독 해지 실패");
                        e.printStackTrace();
                    }
                });
    }

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
    public void sendToOneToken(String targetToken, String title, String body, String clickLink, ApnsPushType apnsPushType, ApnsPriority apnsPriority, AndroidPriority androidPriority) throws FirebaseMessagingException {
        // [START send_to_token]
        // This registration token comes from the client FCM SDKs.
        String registrationToken = targetToken;

        // android 알림 설정
        AndroidConfig.Builder androidConfigBuilder = AndroidConfig.builder();
        // 안드로이드 우선순위 설정
        if(androidPriority != null) {
            androidConfigBuilder.setPriority(AndroidConfig.Priority.valueOf(androidPriority.getValue()));
        } else androidConfigBuilder.setPriority(AndroidConfig.Priority.HIGH);

        // ios 알림 설정
        if(apnsPushType == null) apnsPushType = ApnsPushType.BACKGROUND; // 디폴트
        // ios 푸시타입 설정
        ApnsConfig.Builder apnsConfigBuilder = ApnsConfig.builder()
                .putHeader("apns-push-type", apnsPushType.getValue());
        // ios 우선순위 설정
        if(apnsPriority != null) {
            apnsConfigBuilder.putHeader("apns-priority", apnsPriority.getValue());
        } else apnsConfigBuilder.putHeader("apns-priority", ApnsPriority.FIVE.getValue());

        // See documentation on defining a message payload.
        Message message = Message.builder()
                .setToken(registrationToken)
                .setAndroidConfig(androidConfigBuilder.build())
                .setApnsConfig(apnsConfigBuilder.build())
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
    public void sendFCMToTopics(List<SubscribeType> subscribeTypes, String title, String body, String clickLink, ApnsPushType apnsPushType, ApnsPriority apnsPriority, AndroidPriority androidPriority) throws FirebaseMessagingException {
        // Define a condition which will send to devices which are subscribed
        //String condition = "'stock-GOOG' in topics || 'industry-tech' in topics";

        StringBuilder conditionBuilder = new StringBuilder();
        if(subscribeTypes.size() > 0) {
            conditionBuilder.append("'" + subscribeTypes.get(0).toString() + "' in topics");
        }
        for(int i = 1 ; i < subscribeTypes.size() ; i++) {
            if(i >= 5) break; // 주제는 5개까지만 된다더라
            conditionBuilder.append(" || '" + subscribeTypes.get(i).toString() + "' in topics");
        }

        // android 알림 설정
        AndroidConfig.Builder androidConfigBuilder = AndroidConfig.builder();
        // 안드로이드 우선순위 설정
        if(androidPriority != null) {
            androidConfigBuilder.setPriority(AndroidConfig.Priority.valueOf(androidPriority.getValue()));
        } else androidConfigBuilder.setPriority(AndroidConfig.Priority.HIGH);

        // ios 알림 설정
        if(apnsPushType == null) apnsPushType = ApnsPushType.BACKGROUND; // 디폴트
        // ios 푸시타입 설정
        ApnsConfig.Builder apnsConfigBuilder = ApnsConfig.builder()
                .putHeader("apns-push-type", apnsPushType.getValue());
        // ios 우선순위 설정
        if(apnsPriority != null) {
            apnsConfigBuilder.putHeader("apns-priority", apnsPriority.getValue());
        } else apnsConfigBuilder.putHeader("apns-priority", ApnsPriority.FIVE.getValue());

        // See documentation on defining a message payload.
        Message message = Message.builder()
                .setAndroidConfig(androidConfigBuilder.build())
                .setApnsConfig(apnsConfigBuilder.build())
                .setNotification(Notification.builder()
                        .setTitle(title)
                        .setBody(body)
                        .build())
                .setWebpushConfig(WebpushConfig.builder()
                        .setFcmOptions(WebpushFcmOptions.builder()
                                .setLink(clickLink)
                                .build())
                        .build())
                .setCondition(conditionBuilder.toString())
                .build();

        // Send a message to devices subscribed to the combination of topics
        // specified by the provided condition.
        String response = FirebaseMessaging.getInstance().send(message);
        // Response is a message ID string.
        System.out.println("Successfully sent message: " + response);
    }
}

