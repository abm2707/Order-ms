package com.akhil.orders.event;

import com.akhil.orders.domain.entity.Order;
import com.akhil.orders.domain.valueobject.OrderStatus;
import com.akhil.orders.domain.valueobject.PaymentMethod;
import com.akhil.orders.repository.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.akhil.common.events.*;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
public class OrderSagaConsumer {

    private final OrderRepository orderRepository;
    private final PaymentEventPublisher paymentEventPublisher;

    public OrderSagaConsumer(
            OrderRepository orderRepository,
            PaymentEventPublisher paymentEventPublisher
    ) {
        this.orderRepository = orderRepository;
        this.paymentEventPublisher = paymentEventPublisher;
    }

    // ---------------- Inventory Reserved ----------------

    @Transactional
    @KafkaListener(
            topics = "inventory.reserved",
            groupId = "order-service-group"
    )
    public void onInventoryReserved(StockReservedEvent event) {

        Order order = orderRepository.findById(event.getOrderId()).orElseThrow();

        if (order.getPaymentMethod() == PaymentMethod.COD) {

            orderRepository.updateStatusIfCurrent(
                    event.getOrderId(),
                    OrderStatus.PENDING,
                    OrderStatus.CONFIRMED
            );

            log.info("COD order confirmed → {}", event.getOrderId());

        } else {

            orderRepository.updateStatusIfCurrent(
                    event.getOrderId(),
                    OrderStatus.PENDING,
                    OrderStatus.PAYMENT_PENDING
            );

            paymentEventPublisher.publishPaymentRequest(
                    new PaymentRequestedEvent(
                            event.getOrderId(),
                            order.getTotalAmount(),
                            order.getCurrency()
                    )
            );

            log.info("Payment requested → {}", event.getOrderId());
        }
    }

    // ---------------- Inventory Rejected ----------------

    @Transactional
    @KafkaListener(
            topics = "inventory.rejected",
            groupId = "order-service-group"
    )
    public void onInventoryRejected(StockRejectedEvent event) {

        orderRepository.updateStatusIfCurrent(
                event.getOrderId(),
                OrderStatus.PENDING,
                OrderStatus.CANCELLED
        );

        log.info(
                "Order cancelled due to inventory rejection → {}",
                event.getOrderId()
        );
    }

    // ---------------- Payment Authorized ----------------

    @Transactional
    @KafkaListener(
            topics = "payment.authorized",
            groupId = "order-service-group"
    )
    public void onPaymentAuthorized(PaymentAuthorizedEvent event) {

        orderRepository.updateStatusIfCurrent(
                event.getOrderId(),
                OrderStatus.PAYMENT_PENDING,
                OrderStatus.CONFIRMED
        );

        log.info("Order confirmed after payment → {}", event.getOrderId());
    }

    // ---------------- Payment Failed ----------------

    @Transactional
    @KafkaListener(
            topics = "payment.failed",
            groupId = "order-service-group"
    )
    public void onPaymentFailed(PaymentFailedEvent event) {

        orderRepository.updateStatusIfCurrent(
                event.getOrderId(),
                OrderStatus.PAYMENT_PENDING,
                OrderStatus.CANCELLED
        );

        log.info(
                "Order cancelled due to payment failure → {}",
                event.getOrderId()
        );
    }
}


