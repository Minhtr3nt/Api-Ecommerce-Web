package com.example.ProjectEcommerce.service.cart;

import com.example.ProjectEcommerce.model.CartItem;

public interface ICartItemService {
    void addItemToCart(Long cartId,Long productId, int quantity);
    void removeItemFromCart(Long cartId, Long productId);
    void updateItemQuantity(Long cartId, Long productId, int quantity);

    CartItem getCartItem(Long cartId, Long productId);
}
