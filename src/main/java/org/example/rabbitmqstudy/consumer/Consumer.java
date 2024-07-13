package org.example.rabbitmqstudy.consumer;

import lombok.extern.slf4j.Slf4j;
import org.example.rabbitmqstudy.config.ConfirmConfig;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class Consumer {
    @RabbitListener(queues = ConfirmConfig.CONFIRM_QUEUE_NAME)
    public void receive(Message message) {
        String body = new String(message.getBody());
        log.info("收到内容为: {}", body);
    }
}
