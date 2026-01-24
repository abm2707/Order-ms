package com.akhil.orders.Infrastructure;

import com.akhil.orders.Exceptions.InventoryProductNotFoundException;
import com.akhil.orders.Exceptions.InventoryUnavailableException;
import com.akhil.orders.dto.request.CreateOrderRequest;
import com.akhil.orders.dto.response.InventoryErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import java.time.Duration;

@Component
@Slf4j
public class InventoryClient {

    private final WebClient webClient;

    public InventoryClient(
            WebClient.Builder builder,
            @Value("${inventory.base-url}") String baseUrl
    ) {
        this.webClient = builder.baseUrl(baseUrl).build();
        log.info("InventoryClient initialized with baseUrl={}", baseUrl);
    }

    public InventoryErrorResponse checkAvailability(CreateOrderRequest request) {

        for (var item : request.getItems()) {

            if (item.getProductId() == null || item.getQuantity() <= 0) {
                return new InventoryErrorResponse(
                        "INVALID_REQUEST",
                        "Invalid inventory request for product " + item.getProductId()
                );
            }

            log.info(
                    "Checking inventory → productId={}, quantity={}",
                    item.getProductId(),
                    item.getQuantity()
            );

            InventoryErrorResponse errorResponse =
                    webClient.get()
                            .uri(uriBuilder ->
                                    uriBuilder
                                            .path("/{productId}/availability")
                                            .queryParam("quantity", item.getQuantity())
                                            .build(item.getProductId())
                            )
                            .exchangeToMono(response -> {

                                if (response.statusCode().is2xxSuccessful()) {
                                    return Mono.empty();
                                }

                                return response.bodyToMono(InventoryErrorResponse.class);
                            })
                            .block(Duration.ofSeconds(10));

            // ❗ Fail fast on first inventory error
            if (errorResponse != null) {
                return errorResponse;
            }
        }

        // ✅ All items available
        return null;
    }
}

