package com.allknu.fcm.utils;

import com.google.firebase.messaging.FirebaseMessagingException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
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
            fcmUtil.sendToOneToken(targetToken,"hello", "world!");
        }catch (FirebaseMessagingException firebaseMessagingException) {
            System.out.println(firebaseMessagingException);
        }
        //then

    }
}
