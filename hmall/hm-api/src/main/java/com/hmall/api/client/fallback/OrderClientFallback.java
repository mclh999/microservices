package com.hmall.api.client.fallback;

import com.hmall.api.client.OrderClient;
import com.hmall.common.exception.BadRequestException;
import org.springframework.cloud.openfeign.FallbackFactory;

public class OrderClientFallback implements FallbackFactory<OrderClient> {
    @Override
    public OrderClient create(Throwable cause) {
        return new OrderClient() {
            @Override
            public void markOrderPaySuccess(Long orderId) {
                throw new BadRequestException("支付状态更新失败",cause);
            }
        };
    }
}
