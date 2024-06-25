package com.salam.spring_security.services.order;

import com.salam.spring_security.exception.ResourceNotFoundException;
import com.salam.spring_security.models.cart.Cart;
import com.salam.spring_security.models.cart.CartItems;
import com.salam.spring_security.models.order.Order;
import com.salam.spring_security.models.order.OrderItem;
import com.salam.spring_security.models.order.OrderStatus;
import com.salam.spring_security.models.product.Product;
import com.salam.spring_security.models.user.Role;
import com.salam.spring_security.models.user.User;
import com.salam.spring_security.repository.OrderItemRepository;
import com.salam.spring_security.repository.OrderRepository;
import com.salam.spring_security.services.UserService;
import com.salam.spring_security.services.cart.CartService;
import com.salam.spring_security.services.productService.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderItemRepository orderItemRepository;

    @Mock
    private CartService cartService;

    @Mock
    private ProductService productService;

    @Mock
    private UserService userService;

    @InjectMocks
    private OrderService orderService;

    @BeforeEach
    void setUp() {
        openMocks(this);
    }


    @Test
    void createOrder() {
        // Given
        String username = "testuser";
        User user = User.builder()
                .id(1)
                .username(username)
                .firstname("test")
                .lastName("user")
                .email("test@example.com")
                .password("password")
                .role(Role.USER)
                .build();

        Product product1 = new Product(1, "Product 1", "Description 1", 10.0, "Category 1", 10);
        Product product2 = new Product(2, "Product 2", "Description 2", 20.0, "Category 2", 20);

        CartItems cartItem1 = new CartItems(1, null, product1, 2);
        CartItems cartItem2 = new CartItems(2, null, product2, 1);

        List<CartItems> cartItems = new ArrayList<>();
        cartItems.add(cartItem1);
        cartItems.add(cartItem2);

        Cart cart = new Cart();
        cart.setCartItems(cartItems);

        when(cartService.getCurrentUserCart(username)).thenReturn(cart);
        when(userService.getCurrentUser(username)).thenReturn(user);
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        Order order = orderService.createOrder(username);

        // Then
        assertNotNull(order);
        assertEquals(user, order.getUser());
        assertEquals(LocalDateTime.now().getDayOfMonth(), order.getOrderDate().getDayOfMonth());
        assertEquals(OrderStatus.PENDING, order.getOrderStatus());
        assertEquals(40.0, order.getTotalAmount());

        verify(cartService, times(1)).getCurrentUserCart(username);
        verify(userService, times(1)).getCurrentUser(username);
        verify(orderRepository, times(1)).save(any(Order.class));
        verify(cartService, times(2)).removeItemFromCart(anyInt(), eq(username));

    }


    @Test
    void getAllOrdersForUser() {
        // Given
        String username = "testuser";
        User user = User.builder()
                .id(1)
                .username(username)
                .firstname("test")
                .lastName("user")
                .email("test@example.com")
                .password("password")
                .role(Role.USER)
                .build();
        Order order1 = Order.builder().id(1).user(user).orderStatus(OrderStatus.PENDING).build();
        Order order2 = Order.builder().id(1).user(user).orderStatus(OrderStatus.PENDING).build();

        when(userService.getCurrentUser(username)).thenReturn(user);
        when(orderRepository.findAllByUser(user)).thenReturn(List.of(order1, order2));

        //when
        List<Order> orders = orderService.getAllOrdersForUser(username);

        // Then
        assertNotNull(orders);
        assertEquals(2, orders.size());
        verify(userService, times(1)).getCurrentUser(username);
        verify(orderRepository, times(1)).findAllByUser(user);

    }

    @Test
    void getOrderById() {
        // Given
        String username = "testuser";
        User user = User.builder()
                .id(1)
                .username(username)
                .firstname("test")
                .lastName("user")
                .email("test@example.com")
                .password("password")
                .role(Role.USER)
                .build();
        Order order = Order.builder().id(1).user(user).orderStatus(OrderStatus.PENDING).build();

        when(userService.getCurrentUser(username)).thenReturn(user);
        when(orderRepository.findByIdAndUser(1, user)).thenReturn(Optional.of(order));

        // When
        Order retrievedOrder = orderService.getOrderById(1, username);

        assertNotNull(retrievedOrder);
        assertEquals(order.getId(), retrievedOrder.getId());
        assertEquals(order.getUser(), retrievedOrder.getUser());

        verify(userService, times(1)).getCurrentUser(username);
        verify(orderRepository, times(1)).findByIdAndUser(1, user);
    }

    @Test
    void getOrderByIdNotFound() {
        // Given
        String username = "testuser";
        User user = User.builder()
                .id(1)
                .username(username)
                .firstname("test")
                .lastName("user")
                .email("test@example.com")
                .password("password")
                .role(Role.USER)
                .build();

        when(userService.getCurrentUser(username)).thenReturn(user);
        when(orderRepository.findByIdAndUser(10, user)).thenReturn(Optional.empty());

        // When / Then
        assertThrows(ResourceNotFoundException.class,
                () -> orderService.getOrderById(10, username),
                "Expected ResourceNotFoundException to be thrown");

        verify(userService, times(1)).getCurrentUser(username);
        verify(orderRepository, times(1)).findByIdAndUser(10, user);
    }

}