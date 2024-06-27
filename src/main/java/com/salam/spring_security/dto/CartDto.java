package com.salam.spring_security.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CartDto {
    private Long id;
    private Long userId;
    private List<CartItemDto> cartItems;
}
