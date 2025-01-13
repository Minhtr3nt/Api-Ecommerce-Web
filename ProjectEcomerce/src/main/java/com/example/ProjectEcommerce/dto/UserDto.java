package com.example.ProjectEcommerce.dto;

import com.example.ProjectEcommerce.model.Cart;
import com.example.ProjectEcommerce.model.Order;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class UserDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private List<OrderDto> orders;
    private CartDto cart;
}
