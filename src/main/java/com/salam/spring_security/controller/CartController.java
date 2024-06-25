package com.salam.spring_security.controller;

import com.salam.spring_security.models.cart.Cart;
import com.salam.spring_security.models.cart.CartItems;
import com.salam.spring_security.services.cart.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;

    @Autowired
    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping
    public ResponseEntity<Cart> getCurrentUserCart() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        String username = userDetails.getUsername();
        System.out.println(username);
        Cart cart = cartService.getCurrentUserCart(username);
        return ResponseEntity.ok(cart);
    }

    @PostMapping("/items")
    public ResponseEntity<Cart> addItemToCart(@RequestParam Integer productId,
                                              @RequestParam int quantity) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        String username = userDetails.getUsername();
        Cart cart = cartService.addItemToCart(productId, quantity, username);
        return new ResponseEntity<>(cart, HttpStatus.CREATED);
    }

    @PutMapping("/items/{cartItemId}")
    public ResponseEntity<CartItems> updateCartItemQuantity(@PathVariable Integer cartItemId,
                                                       @RequestParam int quantity) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        String username = userDetails.getUsername();
        CartItems item = cartService.updateCartItemQuantity(cartItemId, quantity, username);
        return new ResponseEntity<>(item, HttpStatus.CREATED);
//        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/items/{cartItemId}")
    public ResponseEntity<Void> removeItemFromCart(@PathVariable Integer cartItemId) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        String username = userDetails.getUsername();
        cartService.removeItemFromCart(cartItemId, username);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }
}
