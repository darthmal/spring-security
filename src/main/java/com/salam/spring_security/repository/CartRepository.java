package com.salam.spring_security.repository;

import com.salam.spring_security.models.cart.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Integer> {
}
