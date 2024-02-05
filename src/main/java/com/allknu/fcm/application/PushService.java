package com.allknu.fcm.application;

import com.allknu.fcm.application.dto.PushToTopicsRequestDto;
import com.allknu.fcm.domain.AndroidPriority;
import com.allknu.fcm.domain.ApnsPriority;
import com.allknu.fcm.domain.ApnsPushType;
import com.allknu.fcm.domain.SubscribeType;
import com.allknu.fcm.application.dto.UpdateSubscribeTopicsRequestDto;
import com.allknu.fcm.global.exception.errors.PushMessageFailedException;
import com.allknu.fcm.global.exception.errors.UnsubscribeFailedException;
import com.allknu.fcm.global.exception.errors.UpdateSubscribeTopicFailedException;
import com.google.firebase.messaging.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;


@Slf4j
@Service
public class PushService {

    // 기본 설정을 해준 메시지 빌더 반환
    private Message.Builder defaultMessageBuilder(String title, String body, String clickLink, ApnsPushType apnsPushType, ApnsPriority apnsPriority, AndroidPriority androidPriority) {
        /* android 알림 설정 */
        AndroidConfig.Builder androidConfigBuilder = AndroidConfig.builder();
        // 안드로이드 우선순위 설정
        if(androidPriority != null) {
            androidConfigBuilder.setPriority(AndroidConfig.Priority.valueOf(androidPriority.getValue()));
        } else androidConfigBuilder.setPriority(AndroidConfig.Priority.HIGH);
        /* //////android 알림 설정 끝////// */

        /* ios 알림 설정 */

        if(apnsPushType == null) apnsPushType = ApnsPushType.BACKGROUND; // 디폴트 설정
        // ios 푸시타입 및 content-available true 설정
        ApnsConfig.Builder apnsConfigBuilder = ApnsConfig.builder()
                .setAps(Aps.builder()
                        .setContentAvailable(true)
                        .build())
                .putHeader("apns-push-type", apnsPushType.getValue());
        // ios 우선순위 설정
        if(apnsPriority != null) {
            apnsConfigBuilder.putHeader("apns-priority", apnsPriority.getValue());
        } else apnsConfigBuilder.putHeader("apns-priority", ApnsPriority.FIVE.getValue());

        /* ///ios 알림 설정 끝///// */

        /* data 설정 */
        Map<String, String> data = new HashMap<>();
        data.put("link", clickLink);

        /* // data 설정 끝 // */
        // See documentation on defining a message payload.
        return Message.builder()
                .setAndroidConfig(androidConfigBuilder.build())
                .setApnsConfig(apnsConfigBuilder.build())
                .putAllData(data)
                .setNotification(Notification.builder()
                        .setTitle(title)
                        .setBody(body)
                        .build())
                .setWebpushConfig(WebpushConfig.builder()
                        .setFcmOptions(WebpushFcmOptions.builder()
                                .setLink(clickLink)
                                .build())
                        .build());
    }

    //unsubscribe from all topic
    private void unsubscribeFromAllTopics(List<String> tokens) {
        //해당 토큰들의 모든 구독을 취소한다.
        EnumSet.allOf(SubscribeType.class)
                .forEach(type -> {
                    try {
                        TopicManagementResponse response = FirebaseMessaging.getInstance()
                                .unsubscribeFromTopic(tokens, type.toString());

                        log.info(response.getSuccessCount() + " tokens were unsubscribed successfully");
                    } catch (FirebaseMessagingException e) {
                        throw new UnsubscribeFailedException();
                    }
                });
    }

    //토큰들을 주제에 구독하는 메서드
    private void subscribeTopic(List<String> tokens, String topic) throws FirebaseMessagingException {
        TopicManagementResponse response = FirebaseMessaging.getInstance().subscribeToTopic(tokens, topic);
        log.info(response.getSuccessCount() + " tokens were subscribed successfully");
    }

    // 공식 문서에 보면 토픽 지정 보내기, 여러 토큰에 한번에 보내기가 있다. 필요할 때마다 추가해 사용하자드
    //특정 토큰에 메시지 전송
    public void sendToOneToken(String targetToken, String title, String body, String clickLink, ApnsPushType apnsPushType, ApnsPriority apnsPriority, AndroidPriority androidPriority) throws FirebaseMessagingException {
        // 기본 메시지 빌더 불러오기
        Message.Builder messageBuilder = defaultMessageBuilder(title, body, clickLink, apnsPushType, apnsPriority, androidPriority);

        messageBuilder.setToken(targetToken);

        String response = FirebaseMessaging.getInstance().send(messageBuilder.build());
        log.info("Successfully sent message: " + response);
    }

    //토픽들에게 메시지 전송
    public void pushToTopics(PushToTopicsRequestDto requestDto) {
        // Define a condition which will send to devices which are subscribed
        //String condition = "'stock-GOOG' in topics || 'industry-tech' in topics";

        StringBuilder conditionBuilder = new StringBuilder();
        if(!requestDto.getSubscribeTypes().isEmpty()) {
            conditionBuilder.append("'" + requestDto.getSubscribeTypes().getFirst().toString() + "' in topics");
        }
        for(int i = 1 ; i < requestDto.getSubscribeTypes().size() ; i++) {
            if(i >= 5) break; // 주제는 5개까지만 된다더라
            conditionBuilder.append(" || '" + requestDto.getSubscribeTypes().get(i).toString() + "' in topics");
        }

        // 기본 메시지 빌더 불러오기
        Message.Builder messageBuilder = defaultMessageBuilder(requestDto.getTitle(),
                requestDto.getBody(),
                requestDto.getClickLink(),
                requestDto.getApnsPushType(),
                requestDto.getApnsPriority(),
                requestDto.getAndroidPriority());

        // 토픽 할당
        messageBuilder.setCondition(conditionBuilder.toString());

        // Send a message to devices subscribed to the combination of topics
        // specified by the provided condition.
        try {
            String response = FirebaseMessaging.getInstance()
                    .send(messageBuilder.build());
            // Response is a message ID string.
            log.info("Successfully sent message: " + response);
        } catch (FirebaseMessagingException exception) {
            throw new PushMessageFailedException();
        }
    }

    public void updateSubscribeTopics(UpdateSubscribeTopicsRequestDto requestDto) {
        try {
            unsubscribeFromAllTopics(Collections.singletonList(requestDto.getToken())); // 구독 전에 기존 모든 구독을 해지한다.

            List<SubscribeType> topics = requestDto.getSubscribes();
            if(topics != null) {
                for (SubscribeType topic : topics) {
                    subscribeTopic(Collections.singletonList(requestDto.getToken()), topic.toString()); // 반복문으로 토픽 구독
                }
            }
        }catch (FirebaseMessagingException firebaseMessagingException) {
            throw new UpdateSubscribeTopicFailedException();
        }
    }
}

