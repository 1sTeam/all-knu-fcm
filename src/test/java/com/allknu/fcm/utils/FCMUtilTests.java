package com.allknu.fcm.utils;

import com.allknu.fcm.core.types.SubscribeType;
import com.allknu.fcm.kafka.dto.FCMWebMessage;
import com.google.firebase.messaging.FirebaseMessagingException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.List;

@SpringBootTest
@ActiveProfiles("local")
public class FCMUtilTests {
    @Autowired
    private FCMUtil fcmUtil;

    @DisplayName("FCM 메시지 전송 테스트")
    @Test
    void sendToTokenFCMTest() {
        //given
        String targetToken = "feelz-2ZH-kiOMNHF9dLC0:APA91bEHgfbHDp5l0n3OMlFcdb2yazuPnaPuTXwUPUOkzc2KxAqZJU8mbh5D4Rfiy9tRim-WfsYKdZ6BT-UVxV9a4gtreWpygJiYG_b6gCrAMZ9HiXYckQdXKWpNXU9zyxsrpN9Xo_lF";
        //when
        try {
            fcmUtil.sendToOneToken(targetToken,"hello", "world!","https://naver.com");
        }catch (FirebaseMessagingException firebaseMessagingException) {
            System.out.println(firebaseMessagingException);
        }
        //then

    }
    @DisplayName("주제 구독 테스트")
    @Test
    void subscribeTopicTest() {
        //given
        List<String> tokens = Arrays.asList("feelz-2ZH-kiOMNHF9dLC0:APA91bEHgfbHDp5l0n3OMlFcdb2yazuPnaPuTXwUPUOkzc2KxAqZJU8mbh5D4Rfiy9tRim-WfsYKdZ6BT-UVxV9a4gtreWpygJiYG_b6gCrAMZ9HiXYckQdXKWpNXU9zyxsrpN9Xo_lF");

        //when
        try {
            fcmUtil.subscribeTopic(tokens, SubscribeType.CAREER.toString());
        }catch (FirebaseMessagingException firebaseMessagingException) {
            System.out.println(firebaseMessagingException);
        }
        //then

    }
    @DisplayName("구독 여러번 테스트")
    @Test
    void subscribeTopicsTest() {
        //given
        List<String> tokens = Arrays.asList("feelz-2ZH-kiOMNHF9dLC0:APA91bEHgfbHDp5l0n3OMlFcdb2yazuPnaPuTXwUPUOkzc2KxAqZJU8mbh5D4Rfiy9tRim-WfsYKdZ6BT-UVxV9a4gtreWpygJiYG_b6gCrAMZ9HiXYckQdXKWpNXU9zyxsrpN9Xo_lF");
        List<SubscribeType> topics = Arrays.asList(SubscribeType.CAREER, SubscribeType.SOFTWARE);
        //when
        try {
            for(int i = 0 ; i < topics.size() ; i++) {
                fcmUtil.subscribeTopic(tokens, topics.get(i).toString());
            }
        }catch (FirebaseMessagingException firebaseMessagingException) {
            System.out.println(firebaseMessagingException);
        }
    }
    @DisplayName("메시지 주제 전송 테스트")
    @Test
    void sendFCMToTopicsTest() {
        //given
        FCMWebMessage fcmWebMessage = FCMWebMessage.builder()
                .subscribeTypes(Arrays.asList(SubscribeType.CAREER, SubscribeType.COUNSEL))
                .title("hello")
                .body("world")
                .clickLink("https://naver.com")
                .build();

        //when
        try {
            fcmUtil.sendFCMToTopics(fcmWebMessage);
        }catch (FirebaseMessagingException firebaseMessagingException) {
            System.out.println(firebaseMessagingException);
        }

        //then

    }
}
