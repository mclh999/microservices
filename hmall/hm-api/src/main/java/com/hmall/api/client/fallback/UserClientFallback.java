package com.hmall.api.client.fallback;

import com.hmall.api.client.UserClient;
import com.hmall.common.exception.BizIllegalException;
import org.springframework.cloud.openfeign.FallbackFactory;

public class UserClientFallback implements FallbackFactory<UserClient> {
    @Override
    public UserClient create(Throwable cause) {
        return new UserClient() {
            @Override
            public void deductMoney(String pw, Integer amount) {
                throw new BizIllegalException("扣减余额失败",cause);
            }
        };
    }
}
