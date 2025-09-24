package com.hmall.trade.listener;

import com.hmall.trade.domain.po.Order;
import com.hmall.trade.service.IOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PayListener {

    private final IOrderService orderService;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "trade.pay.success.queue", durable = "true"),
            exchange = @Exchange(value = "pay.direct"),
            key = "pay.success"
    ))
    public void listenPaySuccess(Long orderId){
        //查询订单状态
        Order order = orderService.getById(orderId);

        //判断当前订单状态是否为未支付，不是则说明当前消息为重复消息
        if(order.getStatus() != 1 || order == null){
            return;
        }

        orderService.markOrderPaySuccess(orderId);//监听到消息，更改订单状态为已支付
    }

}
