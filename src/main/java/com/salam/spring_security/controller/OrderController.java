package com.salam.spring_security.controller;


import com.salam.spring_security.models.order.Order;
import com.salam.spring_security.services.order.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public ResponseEntity<List<Order>> getAllUsersOrder() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        String username = userDetails.getUsername();

        return new ResponseEntity<>(orderService.getAllOrdersForUser(username), HttpStatus.ACCEPTED);
    }

    @GetMapping("{id}")
    public ResponseEntity<Order> getOrderbyId(@PathVariable Integer id) {

        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        String username = userDetails.getUsername();
        return new ResponseEntity<>(orderService.getOrderById(id, username), HttpStatus.FOUND);
    }

    @PostMapping
    public ResponseEntity<Order> newOrder() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        String username = userDetails.getUsername();
        return new ResponseEntity<>(orderService.createOrder(username), HttpStatus.CREATED);
    }
}
