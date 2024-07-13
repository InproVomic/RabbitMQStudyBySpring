package org.example.rabbitmqstudy.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class ConfirmConfig {
    // 要现在application文件中设置spring.rabbitmq.publisher-confirm-type=correlated
    public static final String CONFIRM_EXCHANGE_NAME = "confirm_exchange";
    public static final String CONFIRM_QUEUE_NAME = "confirm_queue";
    public static final String CONFIRM_ROUTING_KEY = "key1";
    // 声明交换机
    @Bean
    public DirectExchange confirmExchange() {
        return ExchangeBuilder.directExchange(CONFIRM_EXCHANGE_NAME).build();
    }
    
    // 声明队列
    @Bean
    public Queue confirmQueue() {
        return QueueBuilder.durable(CONFIRM_QUEUE_NAME).build();
    }

    // 绑定交换机和队列
    @Bean
    public Binding confirmBinding(@Qualifier("confirmExchange") DirectExchange exchange,
                                  @Qualifier("confirmQueue") Queue queue) {
        // 这里因为用的不是自定义的交换机，所以不需要加noargs();
        return BindingBuilder.bind(queue).to(exchange).with(CONFIRM_ROUTING_KEY);
    }
}
