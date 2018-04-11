package com.rabbit.rabbit.customer;

import com.rabbit.rabbit.config.RabbitConfig;
import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.io.IOException;

/***
 *  Created with IntelliJ IDEA.
 *  Author:  wenlin
 *  Date:  2018/4/10 15:09
 *  Description:消费者节点二
 **/
@Component
public class RabbitReciver2 {
    private static final Logger log= LoggerFactory.getLogger(RabbitReciver2.class);
    /**
     * direct队列监听一.
     *
     * @param message the message
     * @param channel the channel
     * @throws IOException the io exception  这里异常需要处理
     */
    @RabbitListener(queues = RabbitConfig.USER_QUEUE_1)
    public void receiver1(Message message, Channel channel) throws IOException {
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        log.debug("节点二USER_QUEUE_1 "+new String(message.getBody()));
    }

    /**
     * direct队列监听二.
     *
     * @param message the message
     * @param channel the channel
     * @throws IOException the io exception  这里异常需要处理
     */
    @RabbitListener(queues = RabbitConfig.USER_QUEUE_2)
    public void receiver2(Message message, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long tag) throws IOException {
        //channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        channel.basicNack(tag, false,true);
        log.debug("节点二USER_QUEUE_2 "+new String(message.getBody()));
    }
}
