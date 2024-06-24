package com.salam.spring_security.repository;

import com.salam.spring_security.models.order.Order;
import com.salam.spring_security.models.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Integer> {
    List<Order> findAllByUser(User user);

    Optional<Order> findByIdAndUser(Integer id, User user);
}
