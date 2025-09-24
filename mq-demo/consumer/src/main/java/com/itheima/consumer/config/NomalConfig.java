package com.itheima.consumer.config;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.retry.RepublishMessageRecoverer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NomalConfig {

    @Bean
    public DirectExchange nomalExchange() {
        return new DirectExchange("nomal.exchange");
    }

    @Bean
    public Queue nomalQueue() {
        //给队列设置死信交换机
        return QueueBuilder
                .durable("nomal.queue")
                .deadLetterExchange("dlx.exchange")
                .build();
    }

    @Bean
    public Binding nomalQueueBinding(DirectExchange nomalExchange,Queue nomalQueue ){
        return BindingBuilder.bind(nomalQueue).to(nomalExchange()).with("nomal");
    }
}
