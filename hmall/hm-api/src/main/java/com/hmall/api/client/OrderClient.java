package com.hmall.api.client;

import com.hmall.api.client.fallback.OrderClientFallback;
import com.hmall.api.config.DefaultFeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@FeignClient(value = "order-service",configuration = DefaultFeignConfig.class,
        fallbackFactory = OrderClientFallback.class)
public interface OrderClient {

    @PutMapping("/orders/{orderId}")
    void markOrderPaySuccess(@PathVariable("orderId") Long orderId);
}
