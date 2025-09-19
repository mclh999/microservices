package com.itheima.consumer.mq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SpringRabbitListener {

    @RabbitListener(queues = "simple.queue")
    public void listenSimpleQueue(String msg){
        log.info("消费者接收到的消息是：{}",msg);
    }

    @RabbitListener(queues = "fanout.queue1")
    public void listenfanout1(String msg){
        log.info("消费者1接收到的消息是：{}",msg);
    }

    @RabbitListener(queues = "fanout.queue2")
    public void listenfanout2(String msg){
        log.info("消费者2接收到的消息是：{}",msg);
    }

    //基于注解声明和绑定队列和交换机
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "direct.queue1"),
            exchange = @Exchange(name = "direct.exchange",type = ExchangeTypes.DIRECT),
            key = {"red","blue"}
    ))
    public void listendirect1(String msg){
        log.info("消费者1接收到的消息是：{}",msg);
    }

    @RabbitListener(queues = "direct.queue2")
    public void listendirect2(String msg){
        log.info("消费者2接收到的消息是：{}",msg);
    }



}
