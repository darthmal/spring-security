package com.salam.spring_security.services.cart;

import com.salam.spring_security.exception.ResourceNotFoundException;
import com.salam.spring_security.models.cart.Cart;
import com.salam.spring_security.models.cart.CartItems;
import com.salam.spring_security.models.product.Product;
import com.salam.spring_security.models.user.Role;
import com.salam.spring_security.models.user.User;
import com.salam.spring_security.repository.CartItemRepository;
import com.salam.spring_security.repository.CartRepository;
import com.salam.spring_security.services.UserService;
import com.salam.spring_security.services.productService.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class CartServiceTest {
    @Mock
    private CartRepository cartRepository;

    @Mock
    private UserService userService;

    @Mock
    private ProductService productService;

    @Mock
    private CartItemRepository cartItemRepository;

    @InjectMocks
    private CartService cartService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addItemToCartNewItem() {
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

        Cart cart = new Cart();
        cart.setId(1);
        cart.setUser(user);
        user.setCart(cart);

        Product product = new Product(1, "Test Product", "Product Description",
                10.0, "Test Category", 10);

        when(userService.getCurrentUser(username)).thenReturn(user);
        when(productService.getProductById(anyInt())).thenReturn(product);
        when(cartRepository.save(any(Cart.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        Cart updatedCart = cartService.addItemToCart(product.getId(), 2, username);

        // Then
        assertNotNull(updatedCart);
        assertEquals(1, updatedCart.getCartItems().size());
        CartItems cartItem = updatedCart.getCartItems().get(0);
        assertEquals(product, cartItem.getProduct());
        assertEquals(2, cartItem.getQuantity());

        verify(userService).getCurrentUser(username);
        verify(productService).getProductById(product.getId());
        verify(cartRepository).save(any(Cart.class));
    }

    @Test
    void addItemToCartExistingItem() {
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

        Product product = new Product(1, "Test Product", "Product Description",
                10.0, "Test Category", 10);

        CartItems existingCartItem = new CartItems();
        existingCartItem.setId(1);
        existingCartItem.setProduct(product);
        existingCartItem.setQuantity(3);

        Cart cart = new Cart();
        cart.setId(1);
        cart.setUser(user);
        cart.setCartItems(new ArrayList<>(List.of(existingCartItem)));
        user.setCart(cart);

        when(userService.getCurrentUser(username)).thenReturn(user);
        when(productService.getProductById(product.getId())).thenReturn(product);
        when(cartRepository.save(any(Cart.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        Cart updatedCart = cartService.addItemToCart(product.getId(), 2, username);

        // Then
        assertNotNull(updatedCart);
        assertEquals(1, updatedCart.getCartItems().size()); // Still one item
        CartItems cartItem = updatedCart.getCartItems().get(0);
        assertEquals(product, cartItem.getProduct());
        assertEquals(5, cartItem.getQuantity()); // Quantity is updated

        verify(userService).getCurrentUser(username);
        verify(productService).getProductById(product.getId());
        verify(cartRepository).save(any(Cart.class));
    }


    @Test
    void getCurrentUserCart() {
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

        Cart cart = new Cart();
        cart.setId(1);
        cart.setUser(user);
        user.setCart(cart);

        when(userService.getCurrentUser(username)).thenReturn(user);

        // When
        Cart retrievedCart = cartService.getCurrentUserCart(username);

        // Then
        assertNotNull(retrievedCart);
        assertEquals(cart, retrievedCart);
        verify(userService).getCurrentUser(username);
    }

    @Test
    void updateCartItemQuantity() {
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

        Product product = new Product(1, "Test Product", "Description",
                10.0, "Test Category", 10);

        CartItems cartItem = new CartItems();
        cartItem.setId(1);
        cartItem.setProduct(product);
        cartItem.setQuantity(2);

        Cart cart = new Cart();
        cart.setId(1);
        cart.setUser(user);
        cart.setCartItems(new ArrayList<>(List.of(cartItem)));
        user.setCart(cart);

        when(userService.getCurrentUser(username)).thenReturn(user);
        when(cartItemRepository.save(any(CartItems.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        CartItems updatedCartItem = cartService.updateCartItemQuantity(cartItem.getId(), 5, username);

        // Then
        assertNotNull(updatedCartItem);
        assertEquals(5, updatedCartItem.getQuantity());
        verify(userService).getCurrentUser(username);
        verify(cartItemRepository).save(any(CartItems.class));
    }

    @Test
    void updateCartItemQuantityNotFound() {
        // Given
        String username = "testuser";
        User user = new User();
        user.setCart(new Cart());

        when(userService.getCurrentUser(username)).thenReturn(user);

        // When / Then
        assertThrows(ResourceNotFoundException.class,
                () -> cartService.updateCartItemQuantity(1, 5, username),
                "Expected ResourceNotFoundException to be thrown");

        verify(userService).getCurrentUser(username);
        verify(cartItemRepository, never()).save(any(CartItems.class)); // Ensure save is never called
    }

    @Test
    void removeItemFromCart() {
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

        Product product = new Product(1, "Test Product", "Description",
                10.0, "Test Category", 10);

        CartItems cartItem = new CartItems();
        cartItem.setId(1);
        cartItem.setProduct(product);
        cartItem.setQuantity(2);

        Cart cart = new Cart();
        cart.setId(1);
        cart.setUser(user);
        cart.setCartItems(new ArrayList<>(List.of(cartItem)));
        user.setCart(cart);


        when(userService.getCurrentUser(username)).thenReturn(user);
        when(cartRepository.save(any(Cart.class))).thenAnswer(invocation -> invocation.getArgument(0));
        // When
        cartService.removeItemFromCart(cartItem.getId(), username);

        // Then
        verify(userService).getCurrentUser(username);
        verify(cartRepository).save(any(Cart.class));
        assertTrue(cart.getCartItems().isEmpty());
    }

    @Test
    void removeItemFromCartNotFound() {
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

        Cart cart = new Cart();
        cart.setId(1);
        cart.setUser(user);
        user.setCart(cart);

        when(userService.getCurrentUser(username)).thenReturn(user);

        // When / Then
        assertThrows(ResourceNotFoundException.class,
                () -> cartService.removeItemFromCart(1, username),
                "Expected ResourceNotFoundException to be thrown");

        verify(userService).getCurrentUser(username);
        verify(cartRepository, never()).save(any(Cart.class)); // Ensure cartRepository.save is not called
    }
}