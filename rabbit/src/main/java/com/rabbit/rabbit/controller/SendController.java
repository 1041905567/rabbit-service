package com.rabbit.rabbit.controller;

import com.rabbit.rabbit.config.RabbitConfig;
import com.rabbit.rabbit.po.User;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/***
 *  Created with IntelliJ IDEA.
 *  Author:  wenlin
 *  Date:  2018/4/10 14:52
 *  Description:发送消息controller
 **/
@RestController
@RequestMapping("send")
public class SendController {
    @Autowired
    private AmqpTemplate amqpTemplate;


    @RequestMapping(value = "direct",method = RequestMethod.GET)
    public String directSend(){
        User user = new User();
        user.setUserName("zhangsan");
        user.setUserId(UUID.randomUUID().toString());
        amqpTemplate.convertAndSend(RabbitConfig.exchange,RabbitConfig.directRouteKey,user);
       return "success";
    }
}
