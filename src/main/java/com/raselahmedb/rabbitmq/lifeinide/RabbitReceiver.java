package com.raselahmedb.rabbitmq.lifeinide;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
//@RabbitListener(queues = {"myqueue"})
public class RabbitReceiver {

    public static final Logger logger = LoggerFactory.getLogger(RabbitReceiver.class);

    @Autowired
    protected PlatformTransactionManager transactionManager;

//    @RabbitHandler
    public void receive(String event) {
        logger.info("Receiving message: {} with transaction manager: {}", event, transactionManager.getClass().getSimpleName());
    }

}