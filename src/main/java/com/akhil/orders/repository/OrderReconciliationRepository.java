package com.akhil.orders.repository;

import com.akhil.orders.domain.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrderReconciliationRepository extends JpaRepository<Order, UUID> {

    @Query(value = """
        SELECT Id
        FROM orders
        WHERE status IN ('PENDING', 'PAYMENT_PENDING')
          AND last_state_updated_at < :cutoff
          AND reconciliation_lock = FALSE
        ORDER BY last_state_updated_at
        LIMIT :limit
        FOR UPDATE SKIP LOCKED
        """, nativeQuery = true)
    List<UUID> findStuckOrdersForUpdate(
            @Param("cutoff") Instant cutoff,
            @Param("limit") int limit
    );

    @Modifying
    @Query(value = """
        UPDATE orders
        SET reconciliation_lock = TRUE,
            reconciliation_attempts = reconciliation_attempts + 1,
            last_reconciliation_at = NOW()
        WHERE Id = :orderId
        """, nativeQuery = true)
    void lockOrder(@Param("orderId") UUID orderId);

    @Modifying
    @Query(value = """
        UPDATE orders
        SET reconciliation_lock = FALSE
        WHERE Id = :orderId
        """, nativeQuery = true)
    void unlockOrder(@Param("orderId") UUID orderId);

    @Modifying
    @Query(value = """
        UPDATE orders
        SET status = :toStatus,
            reconciliation_lock = FALSE,
            last_state_updated_at = NOW()
        WHERE Id = :orderId
          AND status = :fromStatus
        """, nativeQuery = true)
    int transitionState(
            @Param("orderId") UUID orderId,
            @Param("fromStatus") String fromStatus,
            @Param("toStatus") String toStatus
    );

    @Query("""
    SELECT o FROM Order o
    WHERE o.Id = :orderId
    """)
    Optional<Order> findOrder(@Param("orderId") UUID orderId);

}

