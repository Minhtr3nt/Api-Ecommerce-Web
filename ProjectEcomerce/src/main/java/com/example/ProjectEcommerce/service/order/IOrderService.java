package com.example.ProjectEcommerce.service.order;

import com.example.ProjectEcommerce.dto.OrderDto;
import com.example.ProjectEcommerce.model.Order;

import java.util.List;

public interface IOrderService {
    Order placeOrder(Long userId);
    OrderDto getOrder(Long orderId);


    List<OrderDto> getUserOrders(Long userId);

    OrderDto convertToDto(Order order);
}
