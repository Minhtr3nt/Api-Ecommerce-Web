package com.example.ProjectEcommerce.service.cart;

import com.example.ProjectEcommerce.dto.CartDto;
import com.example.ProjectEcommerce.model.Cart;
import com.example.ProjectEcommerce.model.User;

import java.math.BigDecimal;

public interface ICartService {
    Cart getCart(Long id);
    void clearCart(Long id);
    BigDecimal getTotalPrice(Long id);

    Cart initializeNewCart(User user);



    Cart getCartByUserId(Long userId);

    CartDto convertToDto(Cart cart);
}
