package com.akhil.orders.service.Impl;

import com.akhil.orders.domain.valueobject.OrderStatus;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import java.time.Duration;
import java.util.EnumMap;
import java.util.Map;

@Configuration
@ConfigurationProperties(prefix = "reconciliation")
@Getter
@Setter
public class ReconciliationProperties {

    private int batchSize = 50;
    private int maxAttempts = 5;
    private Map<OrderStatus, Duration> sla = new EnumMap<>(OrderStatus.class);
}

