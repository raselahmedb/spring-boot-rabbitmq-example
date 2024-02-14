package com.raselahmedb.rabbitmq.lifeinide;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Binding.DestinationType;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

@RestController
@RequestMapping("/api/rabbit")
public class RabbitController {

    public static final Logger logger = LoggerFactory.getLogger(RabbitController.class);
    
    @Autowired
    protected RabbitTemplate rabbitTemplate;
    @Autowired protected RabbitAdmin rabbitAdmin;
    @Autowired
    protected PlatformTransactionManager transactionManager;

    @GetMapping("/greeting")
    public String greeting() {
        return "greetings";
    }

    @PostConstruct
    protected void init() {
        // define simple exchnage, queue and binding
//        rabbitAdmin.declareExchange(new TopicExchange("myexchange", true, false));
//        rabbitAdmin.declareQueue(new Queue("myqueue", true, false, true, null));
//        rabbitAdmin.declareBinding(new Binding("myqueue", Binding.DestinationType.QUEUE, "myexchange", "myroutingkey", null));
        
    	// first we will define deadletter exchange and queue
        rabbitAdmin.declareExchange(new DirectExchange("deadletter-exchange", true, false));
        rabbitAdmin.declareQueue(new Queue("deadletter-queue", true, false, false, null));
        rabbitAdmin.declareBinding(new Binding("deadletter-queue", DestinationType.QUEUE, "deadletter-exchange", "deadletter-routingkey", null));
        
     // define simple exchange, queue with deadletter support and binding
        rabbitAdmin.declareExchange(new TopicExchange("myexchange", true, false));

        Map<String, Object> args = new HashMap<>();
        args.put("x-dead-letter-exchange", "deadletter-exchange");
        args.put("x-dead-letter-routing-key", "deadletter-routingkey");
        rabbitAdmin.declareQueue(new Queue("myqueue", true, false, true, null));

        rabbitAdmin.declareBinding(new Binding("myqueue", DestinationType.QUEUE, "myexchange", "myroutingkey", args));
        
    }
    
	private void createInit(String key) {
		rabbitAdmin.declareExchange(new TopicExchange("myqueue", true, false));
		rabbitAdmin.declareQueue(new Queue("myqueue", true, false, true, null));
		rabbitAdmin.declareBinding(
				new Binding("myqueue", Binding.DestinationType.QUEUE, "myexchange", "myroutingkey" + key, null));
	}

    @GetMapping(value = "/send")
    @Transactional
    public String send() {
    	String uuid = UUID
    			.randomUUID()
    			.toString()
    			.replaceAll("-", "")
    			.toUpperCase();
    	String key = "" + new Random().nextInt(100);
    	createInit(key);
        String event = "Message: " + uuid;
        logger.info("Sending message: {} with transaction manager: {}", event, transactionManager.getClass().getSimpleName());
        rabbitTemplate.convertAndSend("myexchange", "myroutingkey" + key, event);
        return String.format("Event sent: %s", event);
    }

    @RequestMapping(value = "/send-error")
    @Transactional
    public String sendError() {
        String event = "Message: " + UUID.randomUUID();
        logger.info("Sending message: {} with transaction manager: {}", event, transactionManager.getClass().getSimpleName());
        rabbitTemplate.convertAndSend("myexchange", "myroutingkey", event);
        throw new RuntimeException("Test exception");
    }

    @RequestMapping(value = "/send-receive-error")
    @Transactional
    public String sendReceiveError() {
        String event = "ErrorMessage: " + UUID.randomUUID();
        logger.info("Sending message: {} with transaction manager: {}", event, transactionManager.getClass().getSimpleName());
        rabbitTemplate.convertAndSend("myexchange", "myroutingkey", event);
        return String.format("Event sent: %s", event);
    }

}