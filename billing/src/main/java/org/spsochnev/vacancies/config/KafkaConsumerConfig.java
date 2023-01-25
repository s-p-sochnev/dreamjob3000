package org.spsochnev.vacancies.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@EnableKafka
@Configuration
public class KafkaConsumerConfig {

    @Value(value = "${spring.kafka.bootstrap-servers}")
    private String bootstrapAddress;

    @Value(value = "${vacancy.kafka.billing.group.name}")
    private String billingGroupName;

    @Bean
    public ConsumerFactory<String, StatisticsRequestMessage> consumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(
                ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,
                bootstrapAddress);
        props.put(
                ConsumerConfig.GROUP_ID_CONFIG,
                billingGroupName);
        StringDeserializer keyDeserializer = new StringDeserializer();
        JsonDeserializer valueDeserializer = new JsonDeserializer<>(StatisticsRequestMessage.class);
        DefaultKafkaConsumerFactory defaultKafkaConsumerFactory = new DefaultKafkaConsumerFactory<>(props, keyDeserializer, valueDeserializer);
        return defaultKafkaConsumerFactory;
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, StatisticsRequestMessage> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, StatisticsRequestMessage> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }

}
