package com.ecommerce_micro_service.notification;

import com.ecommerce_micro_service.notification.DTO.OrderCreatedEventDTO;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;


import java.util.Map;

@Service
public class OrderEventConsumer {
    @RabbitListener(queues = "${rabbitmq.queue.name}")
    public void handleOrderEvent(OrderCreatedEventDTO orderCreatedEventDTO){
        System.out.println("Received Order Event:" + orderCreatedEventDTO);
//        Long orderId=Long.valueOf(orderCreatedEventDTO.getOrderId());
//        String status= String.valueOf(orderCreatedEventDTO.getStatus());
        System.out.println("Order ID:"+orderCreatedEventDTO.getOrderId());
        System.out.println("Status : "+orderCreatedEventDTO.getStatus());
    }
}
