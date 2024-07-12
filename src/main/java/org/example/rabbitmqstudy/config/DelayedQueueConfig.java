package org.example.rabbitmqstudy.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.CustomExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Configuration
public class DelayedQueueConfig {
    public static final String DELAYED_QUEUE = "delayed.queue";
    public static final String DELAYED_EXCHANGE = "delayed.exchange";
    public static final String DELAYED_ROUTING_KEY = "delayed.routingkey";
    public static final int DELAYED_TTL = 1000;

    // 声明队列
    @Bean
    public Queue delayedQueue() {
        return new Queue(DELAYED_QUEUE);
    }

    // 声明交换机
    @Bean
    public CustomExchange delayedExchange() {
        // 先设置自定义参数
        Map<String, Object> args = new HashMap<>();
        args.put("x-delayed-type", "direct");
        // 创建自定义交换机
        // 第一个参数：交换机名称 第二个参数：交换机类型 第三个参数：是否持久化 第四个参数：是否自动删除 第五个参数：自定义参数
        return new CustomExchange(DELAYED_EXCHANGE, "x-delayed-message", false, false, args);
    }

    // 绑定交换机和队列
    @Bean
    public Binding delayedBinding(@Qualifier("delayedExchange") CustomExchange delayedExchange,
                                  @Qualifier("delayedQueue") Queue delayedQueue) {
        return BindingBuilder.bind(delayedQueue).to(delayedExchange).with(DELAYED_ROUTING_KEY).noargs();
    }
}
