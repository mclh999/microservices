package com.itheima.consumer.config;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.retry.RepublishMessageRecoverer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class ErrorMessageConfig {
    private final RabbitTemplate rabbitTemplate;

    @Bean
    public DirectExchange errorExchange() {
        return new DirectExchange("error.exchange");
    }

    @Bean
    public Queue errorQueue() {
        return new Queue("error.queue");
    }

    @Bean
    public Binding errorQueueBinding(){
        return BindingBuilder.bind(errorQueue()).to(errorExchange()).with("error");
    }

    @Bean//关联队列和交换机
    public RepublishMessageRecoverer republishMessageRecoverer(){
        return new RepublishMessageRecoverer(rabbitTemplate, "error.exchange", "error");
    }
}
