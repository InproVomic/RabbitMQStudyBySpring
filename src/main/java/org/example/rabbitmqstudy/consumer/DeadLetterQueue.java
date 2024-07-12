package org.example.rabbitmqstudy.consumer;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.example.rabbitmqstudy.config.DelayedQueueConfig;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DeadLetterQueue {
    @RabbitListener(queues = "QD")
    public void receiveD(Message message, Channel channel) throws Exception {
        String msg = new String(message.getBody());
        log.info("接收到的消息为：{}", msg);
    }

    @RabbitListener(queues = DelayedQueueConfig.DELAYED_QUEUE)
    public void receive(Message message, Channel channel) throws Exception {
        String msg = new String(message.getBody());
        log.info("接收到的消息为：{}", msg);
    }
}
