package com.salam.spring_security.models.cart;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.salam.spring_security.models.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "carts")
@Getter
@Setter
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItems> cartItems = new ArrayList<>();

    @Override
    public String toString() {
        return "Cart{" +
                "id=" + id +
                ", cartItems=" + cartItems + // Include cartItems, but not user
                '}';
    }
    // Add convenience methods for cart management
    public void addCartItem(CartItems cartItem) {
        cartItems.add(cartItem);
        cartItem.setCart(this);
    }

    public void removeCartItem(CartItems cartItem) {
        cartItems.remove(cartItem);
        cartItem.setCart(null);
    }

    public Cart(User user) {
        this.user = user;
    }

}
