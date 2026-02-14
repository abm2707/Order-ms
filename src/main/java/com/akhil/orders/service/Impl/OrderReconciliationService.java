package com.akhil.orders.service.Impl;

import com.akhil.orders.domain.entity.Order;
import com.akhil.orders.domain.valueobject.InventoryStatus;
import com.akhil.orders.domain.valueobject.OrderStatus;
import com.akhil.orders.domain.valueobject.PaymentStatus;
import com.akhil.orders.repository.OrderReconciliationRepository;
import com.akhil.orders.service.InventoryVerificationService;
import com.akhil.orders.service.PaymentVerificationService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class OrderReconciliationService {
    private final OrderReconciliationRepository repository;
    private final PaymentVerificationService paymentService;
    private final InventoryVerificationService inventoryService;
    private final ReconciliationProperties properties;

    public OrderReconciliationService(OrderReconciliationRepository repository, PaymentVerificationService paymentService, InventoryVerificationService inventoryService, ReconciliationProperties properties) {
        this.repository = repository;
        this.paymentService = paymentService;
        this.inventoryService = inventoryService;
        this.properties = properties;
    }

    @Transactional
    public void runOnce() {
        Instant cutoff = Instant.now()
                .minus(properties.getSla().get(OrderStatus.PAYMENT_PENDING));

        List<UUID> orderIds =
                repository.findStuckOrdersForUpdate(
                        cutoff,
                        properties.getBatchSize()
                );

        for (UUID orderId : orderIds) {
            repository.lockOrder(orderId);
        }

        for (UUID orderId : orderIds){
            reconcileOrder(orderId);
        }
    }

    public void reconcileOrder(UUID orderId) {

        Order order = repository.findById(orderId)
                .orElseThrow(() ->
                        new IllegalStateException("Order not found " + orderId));

        try {
            log.info("Reconciling order {} in state {}", orderId, order.getStatus());

            if (order.getStatus().isTerminal()) {
                repository.unlockOrder(orderId);
                return;
            }

            switch (order.getStatus()) {
                case PENDING -> handlePending(order);
                case PAYMENT_PENDING -> handlePaymentPending(order);
                default -> repository.unlockOrder(orderId);
            }

            // Always unlock after processing
            repository.unlockOrder(orderId);

        } catch (Exception e) {
            log.error("Reconciliation failed for {}", orderId, e);
            repository.unlockOrder(orderId);
        }
    }


    private void handlePending(Order order) {

        InventoryStatus inventoryStatus =
                inventoryService.verifyReservation(
                        String.valueOf(order.getId()));

        if (inventoryStatus == InventoryStatus.RESERVED
                || inventoryStatus == InventoryStatus.COMMITTED) {

            repository.transitionState(
                    order.getId(),
                    OrderStatus.PENDING.name(),
                    OrderStatus.PAYMENT_PENDING.name()
            );
            return;
        }

        // Inventory never happened → cancel
        repository.transitionState(
                order.getId(),
                OrderStatus.PENDING.name(),
                OrderStatus.CANCELLED.name()
        );
    }

    private void handlePaymentPending(Order order) {

        PaymentStatus paymentStatus =
                paymentService.verifyPayment(
                        order.getPaymentReferenceId());

        switch (paymentStatus) {

            case SUCCESS -> confirmOrder(order);

            case FAILED, NOT_FOUND -> cancelOrder(order);

            case PENDING -> {
                // SLA already breached, so timeout
                cancelOrder(order);
            }
        }
    }
    private void confirmOrder(Order order) {

        int updated = repository.transitionState(
                order.getId(),
                OrderStatus.PAYMENT_PENDING.name(),
                OrderStatus.CONFIRMED.name()
        );

        if (updated == 0) {
            // someone else fixed it
            return;
        }

        log.info("Order {} confirmed by reconciliation", order.getId());
    }

    private void cancelOrder(Order order) {

        int updated = repository.transitionState(
                order.getId(),
                order.getStatus().name(),
                OrderStatus.CANCELLED.name()
        );

        if (updated == 0) {
            return;
        }

        log.info("Order {} cancelled by reconciliation", order.getId());
    }
}
