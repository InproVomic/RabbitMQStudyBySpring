package org.example.rabbitmqstudy.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.rabbitmqstudy.config.DelayedQueueConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@Slf4j
@RestController
@RequestMapping("/ttl")
public class SendMessage {
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @GetMapping("/sendMsg/{message}")
    public void sendMessage(@PathVariable String message) {
        log.info("在"+new Date()+" 发送消息："+message);
        rabbitTemplate.convertAndSend("X", "XA", "消息来自queueA，延时为10s"+message);
        rabbitTemplate.convertAndSend("X", "XB", "消息来自queueB，延时为40s"+message);
    }

    @GetMapping("/sendExpirationMsg/{message}/{ttlTime}")
    public void sendExpirationMessage(@PathVariable String message, @PathVariable String ttlTime) {
        log.info("在"+new Date() +" 发送消息到queueC："+message);
        rabbitTemplate.convertAndSend("X", "XC",
                "消息来自queueC，延时为"+ttlTime+"ms  "+message,msg-> {
            msg.getMessageProperties().setExpiration(ttlTime);
            return msg;
                });
    }

    @GetMapping("/sendDelayMsg/{message}/{delayTime}")
    public void sendDelayMessage(@PathVariable String message, @PathVariable Long delayTime) {
        log.info("当前时间{}，发送消息:{}", new Date(), message);

        rabbitTemplate.convertAndSend(DelayedQueueConfig.DELAYED_EXCHANGE,DelayedQueueConfig.DELAYED_ROUTING_KEY
                ,message,msg->{
            msg.getMessageProperties().setDelayLong(delayTime);
            return msg;
        });
    }
}
