package com.ecommerce_micro_service.order.service;

import com.ecommerce_micro_service.order.client.UserServiceClient;
<<<<<<< HEAD
import com.ecommerce_micro_service.order.dto.OrderCreatedEventDTO;
import com.ecommerce_micro_service.order.dto.OrderItemsDTO;
=======
>>>>>>> 45ce038add230c33c78ad8d28ef5fcb717f61b0c
import com.ecommerce_micro_service.order.dto.UserResponseDTO;
import com.ecommerce_micro_service.order.repositories.CartItemRepository;
import com.ecommerce_micro_service.order.repositories.OrderRepository;
//import com.app.ecom.Repositories.UserRepository;
import com.ecommerce_micro_service.order.dto.OrderResponseDTO;
import com.ecommerce_micro_service.order.exceptions.APIException;
import com.ecommerce_micro_service.order.models.*;
import com.ecommerce_micro_service.order.models.CartItem;
import com.ecommerce_micro_service.order.models.OrderItems;
import com.ecommerce_micro_service.order.models.OrderStatus;
import com.ecommerce_micro_service.order.models.Orders;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
<<<<<<< HEAD
import org.hibernate.query.Order;
import org.modelmapper.ModelMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
=======
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
>>>>>>> 45ce038add230c33c78ad8d28ef5fcb717f61b0c

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final CartService cartService;
    //private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final OrderRepository  orderRepository;
    private final CartItemRepository cartItemRepository;
    private final UserServiceClient userServiceClient;
<<<<<<< HEAD

    private final RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.exchange.name}")
    private String exchangeName;

    @Value("${rabbitmq.routing.key}")
    private String routingKey;

=======
>>>>>>> 45ce038add230c33c78ad8d28ef5fcb717f61b0c
    @Override
    @Transactional
    public OrderResponseDTO placeOrder(String userId) {
        UserResponseDTO userResponse=userServiceClient.getUserById(userId);
        if (userResponse == null ||
                userResponse.getId() == null ||
                userResponse.getName() == null ||
                userResponse.getEmail() == null) {
            throw new APIException("Invalid user response received from UserService");
        }
//        User user=userRepository.findById(Long.parseLong(userId))
//                .orElseThrow(()->new APIException("User not found with userId: "+userId+""));

        List<CartItem>cartItems=cartService.getCartItem(userId).stream()
                .map(item-> {

                    return modelMapper.map(item,CartItem.class);
                }).toList();
        if(cartItems.isEmpty()){
            throw new APIException("Nothing in cart");
        }
        //Integer totalAmount=0;
        //cartItems.forEach(cartItem->totalAmount+=cartItem.getQuantity()*cartItem.getPrice()); // you cant change totalAmount
        //Java treats variables used inside lambdas as final or effectively final, so you can't reassign or mutate totalAmount like that.
        Double totalAmount=0.0;
        for(CartItem cartItem:cartItems){
            totalAmount+= cartItem.getPrice();
        }
        Orders orders=new Orders();
        orders.setUserId(userId);
        orders.setTotalAmount(totalAmount);
        orders.setOrderStatus(OrderStatus.CONFIRMED);
        // create OrderItems
        List<OrderItems> orderItemList=cartItems.stream()
                .map(item->new OrderItems(
                        null,
                        item.getProductId(),
                        item.getQuantity(),
                        item.getPrice(),
                        orders
                )).toList();
        orders.setOrderItems(orderItemList);
        Orders savedOrder=orderRepository.save(orders);
        cartItems.forEach(cartItem->{
            cartItemRepository.deleteById(cartItem.getId());
        });
<<<<<<< HEAD
        OrderCreatedEventDTO orderCreatedEventDTO=new OrderCreatedEventDTO(
                savedOrder.getId(),
                savedOrder.getUserId(),
                savedOrder.getOrderStatus(),
                mapToOrderDTO(savedOrder.getOrderItems()),
                BigDecimal.valueOf(totalAmount),
                savedOrder.getCreatedAt()
        );
        rabbitTemplate.convertAndSend(exchangeName,routingKey, orderCreatedEventDTO);
=======

>>>>>>> 45ce038add230c33c78ad8d28ef5fcb717f61b0c

        OrderResponseDTO orderResponseDTO=modelMapper.map(savedOrder,OrderResponseDTO.class);
        return orderResponseDTO;
    }
<<<<<<< HEAD

    public List<OrderItemsDTO> mapToOrderDTO(List<OrderItems>orderItems){
        return orderItems.stream()
                .map(item->
                        new OrderItemsDTO(
                                item.getId(),
                                item.getProductId(),
                                item.getQuantity(),
                                item.getPrice()
                        )).collect(Collectors.toList());
    }
=======
>>>>>>> 45ce038add230c33c78ad8d28ef5fcb717f61b0c
}
