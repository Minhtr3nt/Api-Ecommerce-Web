package com.example.ProjectEcommerce.repository;

import com.example.ProjectEcommerce.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;



public interface CartRepository extends JpaRepository<Cart, Long> {
    Cart findByUserId(Long userId);
}
