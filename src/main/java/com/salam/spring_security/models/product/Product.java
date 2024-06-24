package com.salam.spring_security.models.product;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "product")
public class Product {

    @Id
    @GeneratedValue
    private Integer id;

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "text") // Use "text" for longer descriptions
    private String description;

    @Column(nullable = false)
    private Double price;

    private String category;

    @Column(nullable = false)
    private int stock;
}
