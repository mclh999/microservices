package com.hmall.cart.listener;

import com.hmall.api.client.CartClient;
import com.hmall.common.utils.UserContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@Slf4j
@RequiredArgsConstructor
public class TradeListener {
    private final CartClient cartClient;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "cart.clear.queue", durable = "true"),
            exchange = @Exchange(value = "trade.topic"),
            key = "order.create"
    ))
    public void listenDeleteCart(Set<Long> itemIds, Message message) {
        // 删除购物车
        Long userId = message.getMessageProperties().getHeader("userId");
        UserContext.setUser(userId);
        cartClient.removeByItemIds(itemIds);
    }
}
