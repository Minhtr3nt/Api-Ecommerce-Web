package com.example.ProjectEcommerce.repository;

import com.example.ProjectEcommerce.model.Order;
import com.example.ProjectEcommerce.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    List<OrderItem> findByProductId(Long id);
}
