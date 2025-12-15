package com.akhil.orders.order_service.event;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class OrderCreatedConsumer {

    @KafkaListener(topics = "order.created",groupId = "order-created-group")
    public void handleOrderCreated(OrderCreatedEvent event) {

        System.out.println(
                "[OrderCreatedConsumer] Received event for orderId = "
                        + event.getOrderId()
        );
    }
}
