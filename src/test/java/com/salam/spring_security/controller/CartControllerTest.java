package com.salam.spring_security.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.salam.spring_security.config.JwtService;
import com.salam.spring_security.models.cart.Cart;
import com.salam.spring_security.models.cart.CartItems;
import com.salam.spring_security.models.product.Product;
import com.salam.spring_security.models.user.Role;
import com.salam.spring_security.models.user.User;
import com.salam.spring_security.services.cart.CartService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CartController.class)
class CartControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CartService cartService;

    @MockBean
    private JwtService jwtService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private WebApplicationContext context;


    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    @WithMockUser(username = "testuser", roles = "USER")
    void getCurrentUserCart() throws Exception {
        //given
        String username = "testuser";
        User user = User.builder()
                .id(1)
                .username(username)
                .firstname("test")
                .lastName("test")
                .password("password")
                .email("test@example.com")
                .role(Role.USER)
                .build();

        Cart cart = new Cart();
        cart.setId(1);
        cart.setUser(user);
        user.setCart(cart);
        when(cartService.getCurrentUserCart(anyString())).thenReturn(cart);

        mockMvc.perform(get("/cart")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(cart.getId())));
    }

    @Test
    @WithMockUser(username = "testuser", roles = "USER")
    void addItemToCart() throws Exception {
        // Given
        String username = "testuser";
        User user = User.builder()
                .id(1)
                .username(username)
                .firstname("test")
                .lastName("test")
                .password("password")
                .email("test@example.com")
                .role(Role.USER)
                .build();

        Product product = new Product(1, "Test Product", "Description", 10.0, "Test Category", 10);
        CartItems cartItem = new CartItems(1, null, product, 2);

        Cart cart = new Cart();
        cart.setId(1);
        cart.setCartItems(List.of(cartItem));
        cart.setUser(user);
        user.setCart(cart);

        when(cartService.addItemToCart(anyInt(), anyInt(), anyString())).thenReturn(cart);

        // When / Then
        mockMvc.perform(post("/cart/items")
                        .param("productId", String.valueOf(product.getId()))
                        .param("quantity", "2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(cart.getId())))
                .andExpect(jsonPath("$.cartItems", hasSize(1)))
                .andExpect(jsonPath("$.cartItems[0].product.id", is(product.getId())));
    }

    @Test
    @WithMockUser(username = "testuser", roles = "USER")
    void updateCartItemQuantity() throws Exception {
        String username = "testuser";
        User user = User.builder()
                .id(1)
                .username(username)
                .firstname("test")
                .lastName("test")
                .password("password")
                .email("test@example.com")
                .role(Role.USER)
                .build();

        Product product = new Product(1, "Test Product", "Description", 10.0, "Test Category", 10);
        CartItems cartItem = new CartItems(1, null, product, 2);
        Cart cart = new Cart();
        cart.setId(1);
        cart.setCartItems(List.of(cartItem));
        cart.setUser(user);
        user.setCart(cart);

        when(cartService.updateCartItemQuantity(anyInt(), anyInt(), anyString())).thenReturn(cartItem);
        // When / Then
        mockMvc.perform(put("/cart/items/{cartItemId}", cartItem.getId())
                        .param("quantity", "5")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(cartItem.getId())))
                .andExpect(jsonPath("$.quantity", is(cartItem.getQuantity())));
    }

    @Test
    @WithMockUser(username = "testuser", roles = "USER")
    void removeItemFromCart() throws Exception {
        // When / Then
        mockMvc.perform(delete("/cart/items/{cartItemId}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted());
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }
}