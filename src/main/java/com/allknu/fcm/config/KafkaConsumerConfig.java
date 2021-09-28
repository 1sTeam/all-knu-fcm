package com.allknu.fcm.config;

import com.allknu.fcm.kafka.dto.FCMSubscribeMessage;
import com.allknu.fcm.kafka.dto.FCMWebMessage;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@EnableKafka
@Configuration
public class KafkaConsumerConfig {
    @Value("${spring.kafka.consumer.group-id}")
    private String groupId;
    @Value("${spring.kafka.consumer.bootstrap-servers}")
    private String brokers;
    @Value("${spring.kafka.consumer.auto-offset-reset}")
    private String offsetReset;

    @Bean
    public NewTopic fcmRequestTopic() {
        return TopicBuilder.name("fcm")
                .partitions(1)
                .replicas(1)
                .build();
    }
    @Bean
    public NewTopic fcmRequestSubscribeTopic() {
        return TopicBuilder.name("fcmSubscribe")
                .partitions(1)
                .replicas(1)
                .build();
    }

    @Bean
    public ConsumerFactory<String, FCMWebMessage> FCMRequestMessageConsumerFactory() {
        Map<String, Object> props = new HashMap<>();
        JsonDeserializer<FCMWebMessage> deserializer = new JsonDeserializer<>(FCMWebMessage.class);
        deserializer.setRemoveTypeHeaders(false);
        deserializer.addTrustedPackages("*");
        deserializer.setUseTypeMapperForKey(true);
        //deserializer.addTrustedPackages("com.example.entity.Foo") // Adding Foo to our trusted packages

        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, brokers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, offsetReset);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, deserializer);
        return new DefaultKafkaConsumerFactory<>(
                props,
                new StringDeserializer(),
                deserializer);
    }

    @Bean
    public ConsumerFactory<String, FCMSubscribeMessage> FCMRequestSubscribeMessageConsumerFactory() {
        Map<String, Object> props = new HashMap<>();
        JsonDeserializer<FCMSubscribeMessage> deserializer = new JsonDeserializer<>(FCMSubscribeMessage.class);
        deserializer.setRemoveTypeHeaders(false);
        deserializer.addTrustedPackages("*");
        deserializer.setUseTypeMapperForKey(true);
        //deserializer.addTrustedPackages("com.example.entity.Foo") // Adding Foo to our trusted packages

        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, brokers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, offsetReset);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, deserializer);
        return new DefaultKafkaConsumerFactory<>(
                props,
                new StringDeserializer(),
                deserializer);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, FCMWebMessage> fcmRequestMessageListener() {
        ConcurrentKafkaListenerContainerFactory<String, FCMWebMessage> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(FCMRequestMessageConsumerFactory());
        return factory;
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, FCMSubscribeMessage> fcmRequestSubscribeMessageListener() {
        ConcurrentKafkaListenerContainerFactory<String, FCMSubscribeMessage> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(FCMRequestSubscribeMessageConsumerFactory());
        return factory;
    }
}
