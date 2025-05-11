package com.ecommerce_micro_service.order.service;

import com.ecommerce_micro_service.order.client.UserServiceClient;
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
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final CartService cartService;
    //private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final OrderRepository  orderRepository;
    private final CartItemRepository cartItemRepository;
    private final UserServiceClient userServiceClient;
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


        OrderResponseDTO orderResponseDTO=modelMapper.map(savedOrder,OrderResponseDTO.class);
        return orderResponseDTO;
    }
}
