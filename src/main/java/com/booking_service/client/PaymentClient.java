package com.booking_service.client;

import com.booking_service.dto.Patient;
import com.booking_service.dto.ProductRequest;
import com.booking_service.dto.StripeResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "payment-service",url = "http://localhost:8084/product/v1")
public interface PaymentClient {

    @GetMapping("/checkout")
    StripeResponse checkoutProducts(@RequestBody ProductRequest productRequest);
}
