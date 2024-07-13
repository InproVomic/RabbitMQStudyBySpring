package org.example.rabbitmqstudy.config;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class MyCallBack implements RabbitTemplate.ConfirmCallback,RabbitTemplate.ReturnsCallback {

    @Autowired
    RabbitTemplate rabbitTemplate;

    @PostConstruct
    public void init() {
        // 将回调方法设置给RabbitTemplate
        rabbitTemplate.setConfirmCallback(this);
        rabbitTemplate.setReturnsCallback(this);
    }
    // 交换机确认回调方法
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        String id  = correlationData!=null?correlationData.getId():"";
        if (ack) {
            log.info("交换机收到了来自id:{}的消息", id);
        } else {
            log.info("交换机没有收到来自id:{}的消息,原因:{}", id, cause);
        }
    }

    @Override
    public void returnedMessage(ReturnedMessage returnedMessage) {
        log.info("消息{},被交换机{}退回,原因:{},路由key:{}"
                ,new String(returnedMessage.getMessage().getBody())
                ,returnedMessage.getExchange(),returnedMessage.getReplyText()
                ,returnedMessage.getRoutingKey());
    }
}
