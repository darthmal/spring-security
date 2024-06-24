package com.salam.spring_security.dto;

import com.salam.spring_security.models.order.OrderItem;
import com.salam.spring_security.models.order.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto {
    private Integer id;
    private Integer userId;
    private List<OrderItem> orderItems;
    private Double totalAmount;
    private LocalDateTime orderDate;
    private OrderStatus orderStatus;
}