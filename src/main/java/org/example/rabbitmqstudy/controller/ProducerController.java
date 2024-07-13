package org.example.rabbitmqstudy.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.rabbitmqstudy.config.ConfirmConfig;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/confirm")
public class ProducerController {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @GetMapping("/sendMessage/{message}")
    public void sendMessage(@PathVariable String message) throws InterruptedException {
        String id = UUID.randomUUID().toString();
        CorrelationData correlationData = new CorrelationData();
        correlationData.setId(id);
        rabbitTemplate.convertAndSend(ConfirmConfig.CONFIRM_EXCHANGE_NAME,
                ConfirmConfig.CONFIRM_ROUTING_KEY, message,correlationData);
        //Thread.sleep(1000);
        //if (correlationData.getReturned() != null) {
        //    log.info("消息发送成功，Id为:{}", id);
        //    log.info("其他内容为{}",new String(correlationData.getReturned().getMessage().getBody()));
        //} else {
        //    log.info("消息发送失败，Id为:{}", id);
        //}
        log.info("发送消息的内容为：{},Id为:{}", message,id);
    }
}
