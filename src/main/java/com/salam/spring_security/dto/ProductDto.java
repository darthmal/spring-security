package com.salam.spring_security.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductDto {
    private String name;

    private String description;

    private String price;

    private String category;

    private String stock;
}
