package com.akhil.orders.event;

import com.akhil.orders.domain.valueobject.OrderStatus;
import com.akhil.orders.repository.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.akhil.common.events.StockRejectedEvent;
import org.akhil.common.events.StockReservedEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
public class InventoryReservedConsumer {

    private final OrderRepository orderRepository;

    public InventoryReservedConsumer(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Transactional
    @KafkaListener(
            topics = "inventory.reserved",
            groupId = "order-service-group"
    )
    public void handleReservation(StockReservedEvent event) {

        log.info("Inventory reserved → orderId={}", event.getOrderId());

        orderRepository.updateStatusIfCurrent(
                event.getOrderId(),
                OrderStatus.PENDING,
                OrderStatus.CONFIRMED
        );
    }

    @Transactional
    @KafkaListener(
            topics = "inventory.rejected",
            groupId = "order-service-group"
    )
    public void handleRejection(StockRejectedEvent event) {

        log.info(
                "Inventory rejected → orderId={}, reason={}",
                event.getOrderId(),
                event.getReason()
        );

        orderRepository.updateStatusIfCurrent(
                event.getOrderId(),
                OrderStatus.PENDING,
                OrderStatus.CANCELLED
        );
    }

}

