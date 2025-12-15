package com.akhil.orders.order_service.event;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class OrderCreatedAuditConsumer {

    @KafkaListener(
            topics = "order.created",
            groupId = "order-created-group"
    )
    public void audit(OrderCreatedEvent event) {

        System.out.println(
                "[AuditConsumer] Auditing orderId = " + event.getOrderId()
        );
    }
}
