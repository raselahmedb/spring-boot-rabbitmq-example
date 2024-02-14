package com.raselahmedb.rabbitmq.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//@Configuration
public class MQConfig {

    public static final String EXCHANGE = "message_exchange2";
    public static final String ROUTING_KEY_PREFIX = "message_routingKey2";

//    @Bean
//    public TopicExchange exchange() {
//        return new TopicExchange(EXCHANGE);
//    }
//
//    @Bean
//    public MessageConverter messageConverter() {
//        return new Jackson2JsonMessageConverter();
//    }
//
//    @Bean
//    public AmqpTemplate template(ConnectionFactory connectionFactory) {
//        RabbitTemplate template = new RabbitTemplate(connectionFactory);
//        template.setMessageConverter(messageConverter());
//        return template;
//    }
//
//    // Add a method to create a user-specific queue
//    @Bean
//    public Queue userQueue(@Value("${user.queue.prefix}") String userQueuePrefix) {
//        String queueName = userQueuePrefix + "_message_queue";
//        return new Queue(queueName);
//    }
//
//    // Add a method to bind user-specific queue to the exchange
//    @Bean
//    public Binding userBinding(Queue userQueue, TopicExchange exchange, @Value("${user.routing.key}") String userRoutingKey) {
//        return BindingBuilder
//                .bind(userQueue)
//                .to(exchange)
//                .with(userRoutingKey);
//    }
}
