package com.salam.spring_security.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartItemDto {
    private Long id;
    private Long cartId;
    private CartItemDto cartItem;
    private int quantity;
}
