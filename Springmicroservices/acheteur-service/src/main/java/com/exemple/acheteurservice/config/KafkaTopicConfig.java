package com.exemple.acheteurservice.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    public static final String ACHAT_EFFECTUE_TOPIC = "achat-effectue-topic";

    @Bean
    public NewTopic achatEffectueTopic() {
        return TopicBuilder.name(ACHAT_EFFECTUE_TOPIC)
                .partitions(1)
                .replicas(1)
                .build();
    }
} 