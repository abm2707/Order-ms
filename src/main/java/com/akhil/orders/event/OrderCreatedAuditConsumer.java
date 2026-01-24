package com.akhil.orders.event;

import lombok.extern.slf4j.Slf4j;
import org.akhil.common.events.OrderCreatedEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class OrderCreatedAuditConsumer {

    @KafkaListener(
            topics = "order.created",
            groupId = "order-created-group",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void audit(OrderCreatedEvent event) {
        log.error("Audit payload: {}", event.toString());
        System.out.println(
                "[AuditConsumer] Auditing orderId = " + event.getOrderId()
        );
    }
}
