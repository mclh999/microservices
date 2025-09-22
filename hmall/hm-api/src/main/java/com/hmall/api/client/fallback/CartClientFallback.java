package com.hmall.api.client.fallback;

import com.hmall.api.client.CartClient;
import com.hmall.common.exception.BadRequestException;
import org.springframework.cloud.openfeign.FallbackFactory;

import java.util.Collection;

public class CartClientFallback implements FallbackFactory<CartClient> {
    @Override
    public CartClient create(Throwable cause) {
        return new CartClient() {
            @Override
            public void removeByItemIds(Collection<Long> ids) {
                throw new BadRequestException("删除失败",cause);
            }
        };
    }
}
