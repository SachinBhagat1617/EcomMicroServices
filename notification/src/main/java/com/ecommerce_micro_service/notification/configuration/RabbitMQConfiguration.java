package com.ecommerce_micro_service.notification.configuration;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;



@Configuration // Ye batata hai Spring ko ki ye class configuration file hai
public class RabbitMQConfiguration {

    // Ye values application.properties file se aayengi
    @Value("${rabbitmq.queue.name}")
    private String queueName;

    @Value("${rabbitmq.exchange.name}")
    private String exchangeName;

    @Value("${rabbitmq.routing.key}")
    private String routingKey;

    // -------------------- QUEUE CONFIGURATION --------------------
    @Bean
    public Queue queue() {
        // Queue durable hone ka matlab ye hai ki RabbitMQ restart hone ke baad bhi queue delete nahi hogi
        // Yani queue data safe rahega (persistent)
        return QueueBuilder.durable(queueName)
                .build();
    }

    // -------------------- EXCHANGE CONFIGURATION --------------------
    @Bean
    public TopicExchange exchange() {
        // Exchange ka kaam hota hai message ko sahi queue tak route karna based on routing key
        // TopicExchange allow karta hai routing key pattern (like user.*, order.#)
        return ExchangeBuilder.topicExchange(exchangeName)
                .durable(true) // Exchange bhi RabbitMQ restart hone ke baad bhi bana rahega
                .build();
    }

    // -------------------- BINDING CONFIGURATION --------------------
    @Bean
    public Binding binding() {
        // Binding ka kaam hai queue ko exchange se jodna ek routing key ke saath
        // Jab message exchange ko bheja jata hai, routing key se decide hota hai kaun si queue me jaye
        return BindingBuilder.bind(queue())
                .to(exchange())
                .with(routingKey);
    }

    // -------------------- RABBIT ADMIN CONFIG --------------------
    @Bean
    public AmqpAdmin amqpAdmin(ConnectionFactory connectionFactory) {
        // RabbitAdmin ek helper class hai jo queues, exchanges, aur bindings ko automatically RabbitMQ me create kar deta hai
        // ConnectionFactory -> RabbitMQ ke server se connection banata hai
        RabbitAdmin admin = new RabbitAdmin(connectionFactory);
        admin.setAutoStartup(true); // Application start hone par ye automatically setup kar dega (queues, exchange, bindings)
        return admin;
    }

    // -------------------- MESSAGE CONVERTER --------------------
    @Bean
    public MessageConverter messageConverter() {
        // Ye converter Java Object ko JSON me convert karta hai aur wapas (deserialize) bhi karta hai
        // Isse hum plain JSON messages ke form me RabbitMQ me data bhej sakte hain
        return new Jackson2JsonMessageConverter();
    }

//    // -------------------- RABBIT TEMPLATE (PRODUCER) --------------------
//    @Bean
    //// we don't need because we are not sending msg instead we are consuming msg
//    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
//        // RabbitTemplate ka kaam hai message bhejna aur receive karna RabbitMQ se
//        RabbitTemplate template = new RabbitTemplate(connectionFactory);
//        // Yahan hum message converter set kar rahe hain taaki Java object -> JSON conversion ho
//        template.setMessageConverter(messageConverter());
//        // Default exchange set kar diya gaya, taaki har bar manually dena na pade
//        template.setExchange(exchangeName);
//        return template;
//    }
}
