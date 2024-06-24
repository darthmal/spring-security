package com.salam.spring_security.repository;

import com.salam.spring_security.models.cart.CartItems;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItems, Integer> {
}
