package com.ecommerce_micro_service.notification;

import com.ecommerce_micro_service.notification.DTO.OrderCreatedEventDTO;
import lombok.extern.slf4j.Slf4j;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;


import java.util.Map;
import java.util.function.Consumer;

@Service
@Slf4j
public class OrderEventConsumer {
//    @RabbitListener(queues = "${rabbitmq.queue.name}")
//    public void handleOrderEvent(OrderCreatedEventDTO orderCreatedEventDTO){
//        System.out.println("Received Order Event:" + orderCreatedEventDTO);
////        Long orderId=Long.valueOf(orderCreatedEventDTO.getOrderId());
////        String status= String.valueOf(orderCreatedEventDTO.getStatus());
//        System.out.println("Order ID:"+orderCreatedEventDTO.getOrderId());
//        System.out.println("Status : "+orderCreatedEventDTO.getStatus());
//    }
    @Bean
    public Consumer<OrderCreatedEventDTO> orderCreated(){ // cloud Function
        return event->{
            log.info("Order Id: {}", event.getOrderId());
            log.info("Received from User Id: {}",event.getUserId());
            System.out.println("Received from User Id: {}"+event.getUserId());
        };
    }


}
