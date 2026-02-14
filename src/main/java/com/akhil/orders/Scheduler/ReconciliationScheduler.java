package com.akhil.orders.Scheduler;

import com.akhil.orders.service.Impl.OrderReconciliationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Component
@RequiredArgsConstructor
@RestController
@RequestMapping("/internal/reconciliation")
public class ReconciliationScheduler {

    private final OrderReconciliationService reconciliationService;

    @Scheduled(fixedDelayString = "PT1M")
    @PostMapping("/run-once")
    public ResponseEntity<String> runOnce() {
        reconciliationService.runOnce();
        return ResponseEntity.ok("Reconciliation triggered");
    }
}

