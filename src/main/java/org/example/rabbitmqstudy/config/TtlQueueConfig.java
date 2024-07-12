package org.example.rabbitmqstudy.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class TtlQueueConfig {
    // 普通交换机名称
    public static final String X_EXCHANGE = "X";
    // 普通队列名称
    public static final String QUEUE_A = "QA";
    public static final String QUEUE_B = "QB";
    public static final String QUEUE_C = "QC";
    // 死信交换机名称
    public static final String Y_EXCHANGE = "Y";
    // 死信队列名称
    public static final String QUEUE_D = "QD";

    @Bean("xExchange")
    public DirectExchange xExchange() {
        return new DirectExchange(X_EXCHANGE);
    }

    @Bean("yExchange")
    public DirectExchange yExchange() {
        return new DirectExchange(Y_EXCHANGE);
    }

    @Bean("queueA")
    public Queue queueA() {
        Map<String, Object> args = new HashMap<String, Object>();
        args.put("x-dead-letter-exchange", Y_EXCHANGE);
        args.put("x-dead-letter-routing-key", "YD");
        args.put("x-message-ttl", 10000); // 一般不这样设置过期时间，一般是在生产者那端设置
        return QueueBuilder.durable(QUEUE_A).withArguments(args).build();
    }

    @Bean("queueB")
    public Queue queueB() {
        Map<String, Object> args = new HashMap<String, Object>();
        args.put("x-dead-letter-exchange", Y_EXCHANGE);
        args.put("x-dead-letter-routing-key", "YD");
        args.put("x-message-ttl", 40000); // 一般不这样设置过期时间，一般是在生产者那端设置
        return QueueBuilder.durable(QUEUE_B).withArguments(args).build();
    }

    @Bean("queueC")
    public Queue queueC() {
        Map<String, Object> args = new HashMap<String, Object>();
        args.put("x-dead-letter-exchange", Y_EXCHANGE);
        args.put("x-dead-letter-routing-key", "YD");
        return QueueBuilder.durable(QUEUE_C).withArguments(args).build();
    }

    @Bean("queueD")
    public Queue queueD() {
        return QueueBuilder.durable(QUEUE_D).build();
    }

    @Bean
    public Binding bindingA(@Qualifier("queueA") Queue queueA
            , @Qualifier("xExchange") DirectExchange xExchange) {
        // 将队列A绑定到交换机X，并且指定路由键为XA
        return BindingBuilder.bind(queueA).to(xExchange).with("XA");
    }

    @Bean
    public Binding bindingB(@Qualifier("queueB") Queue queueB
            , @Qualifier("xExchange") DirectExchange xExchange) {
        return BindingBuilder.bind(queueB).to(xExchange).with("XB");
    }

    @Bean
    public Binding bindingD(@Qualifier("queueD") Queue queueD
            , @Qualifier("yExchange") DirectExchange yExchange) {
        return BindingBuilder.bind(queueD).to(yExchange).with("YD");
    }

    @Bean
    public Binding bindingC(@Qualifier("queueC") Queue queueC
            , @Qualifier("xExchange") DirectExchange xExchange) {
        return BindingBuilder.bind(queueC).to(xExchange).with("XC");
    }
}
