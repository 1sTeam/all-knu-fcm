package com.allknu.fcm.application;

import com.allknu.fcm.domain.AndroidPriority;
import com.allknu.fcm.domain.ApnsPriority;
import com.allknu.fcm.domain.ApnsPushType;
import com.allknu.fcm.domain.SubscribeType;
import com.allknu.fcm.presentation.dto.FcmWebMessage;
import com.google.firebase.messaging.FirebaseMessagingException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.util.Arrays;
import java.util.List;

@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource("classpath:/secrets/test-secrets.properties")
public class FcmServiceTests {
    @Autowired
    private FcmService fcmService;
    @Value("${fcm.token}")
    private String targetToken;

    @DisplayName("FCM 메시지 전송 테스트")
    @Test
    void sendToTokenFCMTest() {
        //given
        //when
        try {
            fcmService.sendToOneToken(targetToken,"hello", "world!","https://naver.com", ApnsPushType.BACKGROUND, ApnsPriority.FIVE, AndroidPriority.HIGH);
        }catch (FirebaseMessagingException firebaseMessagingException) {
            System.out.println(firebaseMessagingException);
        }
        //then

    }
    @DisplayName("주제 구독 테스트")
    @Test
    void subscribeTopicTest() {
        //given
        List<String> tokens = Arrays.asList(targetToken);

        //when
        try {
            fcmService.subscribeTopic(tokens, SubscribeType.CAREER.toString());
        }catch (FirebaseMessagingException firebaseMessagingException) {
            System.out.println(firebaseMessagingException);
        }
        //then

    }
    @DisplayName("구독 여러번 테스트")
    @Test
    void subscribeTopicsTest() {
        //given
        List<String> tokens = Arrays.asList(targetToken);
        List<SubscribeType> topics = Arrays.asList(SubscribeType.CAREER, SubscribeType.SOFTWARE);
        //when
        try {
            for(int i = 0 ; i < topics.size() ; i++) {
                fcmService.subscribeTopic(tokens, topics.get(i).toString());
            }
        }catch (FirebaseMessagingException firebaseMessagingException) {
            System.out.println(firebaseMessagingException);
        }
    }
    @DisplayName("메시지 주제 전송 테스트")
    @Test
    void sendFCMToTopicsTest() {
        //given
        FcmWebMessage fcmWebMessage = FcmWebMessage.builder()
                .subscribeTypes(Arrays.asList(SubscribeType.CAREER, SubscribeType.COUNSEL))
                .title("hello")
                .body("world")
                .clickLink("https://naver.com")
                .build();

        //when
        try {
            fcmService.sendFCMToTopics(fcmWebMessage.getSubscribeTypes(), fcmWebMessage.getTitle(), fcmWebMessage.getBody(), fcmWebMessage.getClickLink(), ApnsPushType.BACKGROUND, ApnsPriority.FIVE, AndroidPriority.HIGH);
        }catch (FirebaseMessagingException firebaseMessagingException) {
            System.out.println(firebaseMessagingException);
        }

        //then

    }

    @DisplayName("구독 해지 테스트")
    @Test
    void unsubscribeFromAllTopicsTest() {
        List<String> tokens = Arrays.asList(targetToken);
        fcmService.unsubscribeFromAllTopics(tokens);
    }

    @DisplayName("구독 및 해지 테스트")
    @Test
    void subscribeAndUnsubscribeFromAllTopicsTest() {
        //given
        List<String> tokens = Arrays.asList(targetToken);
        List<SubscribeType> topics = Arrays.asList(SubscribeType.CAREER, SubscribeType.SOFTWARE);
        try {
            for(int i = 0 ; i < topics.size() ; i++) {
                fcmService.subscribeTopic(tokens, topics.get(i).toString());
            }
        }catch (FirebaseMessagingException firebaseMessagingException) {
            System.out.println(firebaseMessagingException);
            firebaseMessagingException.printStackTrace();
        }
        //when
        fcmService.unsubscribeFromAllTopics(tokens);
        //then
    }
}
