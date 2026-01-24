package com.akhil.orders.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.akhil.common.events.OrderCreatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class OrderEventPublisher {

    private static final Logger log = LoggerFactory.getLogger(OrderEventPublisher.class);
    private static final String ORDER_CREATED_TOPIC = "order.created";
    private final KafkaTemplate<String, OrderCreatedEvent> kafkaTemplate;
    ObjectMapper objectMapper = new ObjectMapper();

    public OrderEventPublisher(KafkaTemplate<String, OrderCreatedEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishOrderCreated(OrderCreatedEvent event) {
        //log.info("Producing OrderCreatedEvent: {}", objectMapper.writeValueAsString(event));
        kafkaTemplate.send(
                ORDER_CREATED_TOPIC,
                event.getOrderId().toString(),
                event
        );
    }
}