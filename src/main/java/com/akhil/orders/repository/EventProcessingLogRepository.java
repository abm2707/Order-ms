package com.akhil.orders.repository;

import com.akhil.orders.domain.entity.EventProcessingLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface EventProcessingLogRepository extends JpaRepository<EventProcessingLog, UUID> {

    List<EventProcessingLog> findByAggregateId(UUID aggregateId);
}