package com.allknu.fcm.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class MessageConsumer {

    @KafkaListener(topics = "fcm", groupId = "all-knu-fcm")
    public void consume(String message) throws IOException {
        System.out.println(String.format("Consumed message : %s", message));
    }
}