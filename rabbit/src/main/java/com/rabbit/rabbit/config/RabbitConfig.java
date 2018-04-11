package com.rabbit.rabbit.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/***
 *  Created with IntelliJ IDEA.
 *  Author:  wenlin
 *  Date:  2018/4/10 14:28
 *  Description:
 **/
@Component
public class RabbitConfig {

    public static final String USER_QUEUE_1 = "userQueue1";

    public static final String USER_QUEUE_2 = "userQueue2";
    //直连路由键名
    public static final String directRouteKey = "userKey";
    //直连交换机
    public static final String exchange = "userExchange";

    @Autowired
    private RabbitTemplate rabbitTemplate;
    /**
     * 此处为模版类定义 Jackson消息转换器
     * ConfirmCallback接口用于实现消息发送到RabbitMQ交换器后接收ack回调   即消息发送到exchange  ack
     * ReturnCallback接口用于实现消息发送到RabbitMQ 交换器，但无相应队列与交换器绑定时的回调  即消息发送不到任何一个队列中
     * @return the amqp template
     */
    @Bean
    public AmqpTemplate amqpTemplate() {
        Logger log = LoggerFactory.getLogger(RabbitTemplate.class);
        //消息转换器
        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
        //编码
        rabbitTemplate.setEncoding("UTF-8");
        rabbitTemplate.setMandatory(true);
        //rabbitTemplate.setR
        //开启消息发送回调
        rabbitTemplate.setReturnCallback((message, replyCode, replyText, exchange, routingKey) -> {
            String correlationId = message.getMessageProperties().getCorrelationId();
            log.info("消息：{} 发送失败, 应答码：{} 原因：{} 交换机: {}  路由键: {}", correlationId, replyCode, replyText, exchange, routingKey);
        });
        // 开启消息确认回调
        rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
            if (ack) {
                log.info("消息发送到exchange成功,id: {}");
            } else {
                log.info("消息发送到exchange失败,原因: {}", cause);
            }
        });
        return rabbitTemplate;
    }
    //队列1
    @Bean
    public Queue buildQueue1() {
    /* 是否持久化
     仅创建者可以使用的私有队列，断开后自动删除
     当所有消费客户端连接断开后，是否自动删除队列*/
        return new Queue(USER_QUEUE_1, true, false, false);
    }
    //队列2
    @Bean
    public Queue buildQueue2() {
        return new Queue(USER_QUEUE_2, true, false, false);
    }


    //定义直连交换机
    @Bean
    public DirectExchange exchange() {
        return new DirectExchange(exchange, true, false);
    }
    //将队列1绑定到直接交换机
    @Bean
    public Binding bindUserTripQueue() {
        return BindingBuilder.bind(buildQueue1()).to(exchange()).with(directRouteKey);
    }
    //将队列2绑定到直接交换机
    @Bean
    public Binding bindUserAccountQueue() {
        return BindingBuilder.bind(buildQueue2()).to(exchange()).with(directRouteKey);
    }

}
