package com.raselahmedb.rabbitmq.config;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.raselahmedb.rabbitmq.models.CustomMessage;

@Component
public class MessageListener {

//    @RabbitListener(queues = MQConfig_Old.QUEUE)
    public void listener(CustomMessage message) {
        System.out.println(message);
    }
    
 // Update the listener to use a dynamic queue name
//    @RabbitListener(queues = "${user.queue.prefix}_message_queue")
//    public void listenerDynamic(CustomMessage message) {
//        System.out.println("Received message: " + message);
//    }

}