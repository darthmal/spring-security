package com.salam.spring_security.services.order;

import com.salam.spring_security.exception.ResourceNotFoundException;
import com.salam.spring_security.models.cart.Cart;
import com.salam.spring_security.models.order.Order;
import com.salam.spring_security.models.order.OrderItem;
import com.salam.spring_security.models.order.OrderStatus;
import com.salam.spring_security.models.user.User;
import com.salam.spring_security.repository.OrderItemRepository;
import com.salam.spring_security.repository.OrderRepository;
import com.salam.spring_security.services.UserService;
import com.salam.spring_security.services.cart.CartService;
import com.salam.spring_security.services.productService.ProductService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final CartService cartService;
    private final ProductService productService;
    private final UserService userService;

    public OrderService(OrderRepository orderRepository, OrderItemRepository orderItemRepository, CartService cartService, ProductService productService, UserService userService) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.cartService = cartService;
        this.productService = productService;
        this.userService = userService;
    }

    public Order createOrder(String username) {
        Cart cart = cartService.getCurrentUserCart(username);

        Order order = new Order();
        order.setUser(userService.getCurrentUser(username));
        order.setOrderDate(LocalDateTime.now());
        order.setOrderStatus(OrderStatus.PENDING);
        Double totalAmount = cart.getCartItems().stream()
                .mapToDouble(cartItems -> cartItems.getProduct().getPrice() * cartItems.getQuantity())
                .sum();
        order.setTotalAmount(totalAmount);
        List<OrderItem> orderItems = cart.getCartItems().stream()
                .map(cartItems -> {
                    OrderItem orderItem = new OrderItem();
                    orderItem.setProduct(cartItems.getProduct());
                    orderItem.setQuantity(cartItems.getQuantity());
                    orderItem.setPrice(cartItems.getProduct().getPrice());
                    return orderItem;
                }).toList();

        orderItems.forEach(order::addOrderItem);
        orderRepository.save(order);

        // Delete all items from the cart
        cart.getCartItems().forEach(cartItems -> {
            cartService.removeItemFromCart(cartItems.getId(), username);
        });
        return order;
    }

    public List<Order> getAllOrdersForUser(String username) {
        User user = userService.getCurrentUser(username);
        return orderRepository.findAllByUser(user);
    }

    public Order getOrderById(Integer orderId, String username) {
        User user = userService.getCurrentUser(username);
        return orderRepository.findByIdAndUser(orderId, user)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "not found", ""));
    }
}
