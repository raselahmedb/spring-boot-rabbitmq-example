package com.raselahmedb.rabbitmq.controllers;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.raselahmedb.rabbitmq.config.MQConfig;
import com.raselahmedb.rabbitmq.config.MQConfig_Old;
import com.raselahmedb.rabbitmq.models.CustomMessage;

import java.util.Date;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class MessagePublisher {

    @Autowired
    private RabbitTemplate template;

    @PostMapping("/publish")
    public String publishMessage(@RequestBody CustomMessage message) {
        message.setMessageId(UUID.randomUUID().toString());
        message.setMessageDate(new Date());
        template.convertAndSend(MQConfig_Old.EXCHANGE,
                MQConfig_Old.ROUTING_KEY, message);

        return "Message Published";
    }
    
 // Update the method to take user-specific parameters
    @PostMapping("/publish2/{userId}")
    public String publishMessage(@PathVariable String userId, @RequestBody CustomMessage message) {
        message.setMessageId(UUID.randomUUID().toString());
        message.setMessageDate(new Date());
        
        // Use user-specific routing key
        String userRoutingKey = MQConfig.ROUTING_KEY_PREFIX + "." + userId;
        
        // Publish the message to the user-specific exchange and routing key
        template.convertAndSend(MQConfig.EXCHANGE, userRoutingKey, message);

        return "Message Published";
    }
}