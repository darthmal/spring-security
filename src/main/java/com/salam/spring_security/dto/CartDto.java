package com.salam.spring_security.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CartDto {
    private Long id;
    private Long userId; // You might use UserDto here for more details
    private List<CartItemDto> cartItems;
}
