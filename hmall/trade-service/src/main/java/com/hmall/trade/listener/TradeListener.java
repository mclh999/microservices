package com.hmall.trade.listener;

import com.hmall.api.client.PayClient;
import com.hmall.api.dto.PayOrderDTO;
import com.hmall.trade.constants.Mqconstants;
import com.hmall.trade.domain.po.Order;
import com.hmall.trade.service.IOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RabbitListener
@RequiredArgsConstructor
public class TradeListener {
    private final IOrderService OrderService;
    private final PayClient payClient;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = Mqconstants.DELAY_ORDER_QUEUE_NAME, durable = "true"),
            exchange = @Exchange(value = Mqconstants.DELAY_EXCHANGE_NAME),
            key = Mqconstants.DELAY_ORDER_KEY
    ))
    public void listenPaySuccess(Long orderId) {
        // 1.查询订单
        Order order = OrderService.getById(orderId);

        // 2.已支付，直接跳过
        if(order.getStatus() != 1 || order == null){
            return;
        }

        // 3.未支付，查询支付流水
        PayOrderDTO payOrder = payClient.queryPayOrderByBizOrderNo(orderId);

        // 4.判断是否支付
        if(payOrder != null && payOrder.getStatus() == 3){
            // 4.1 已支付，标记订单为已支付
            OrderService.markOrderPaySuccess(orderId);

        }else{
            // 4.2 未支付，取消订单，回滚库存
            OrderService.cancelOrder(orderId);

        }
    }
}
