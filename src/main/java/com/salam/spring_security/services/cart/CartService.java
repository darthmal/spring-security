package com.salam.spring_security.services.cart;


import com.salam.spring_security.exception.ResourceNotFoundException;
import com.salam.spring_security.models.cart.Cart;
import com.salam.spring_security.models.cart.CartItems;
import com.salam.spring_security.models.product.Product;
import com.salam.spring_security.models.user.User;
import com.salam.spring_security.repository.CartItemRepository;
import com.salam.spring_security.repository.CartRepository;
import com.salam.spring_security.services.UserService;
import com.salam.spring_security.services.productService.ProductService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CartService {

    private final CartRepository cartRepository;
    private final UserService userService;
    private final ProductService productService;
    private final CartItemRepository cartItemRepository;

    public CartService(CartRepository cartRepository, UserService userService, ProductService productService, CartItemRepository cartItemRepository) {
        this.cartRepository = cartRepository;
        this.userService = userService;
        this.productService = productService;
        this.cartItemRepository = cartItemRepository;
    }

    // Add item to cart
    public Cart addItemToCart(Integer productId, int quantity, String username) {
        Cart cart = getCurrentUserCart(username);
        Product product = productService.getProductById(productId);

        // Check if the product is already in the cart
        CartItems cartItem = cart.getCartItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst()
                .orElse(null);

        if (cartItem != null) {
            // Update quantity if product exists
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
        } else {
            // Add new item to cart
            cartItem = new CartItems();
            cartItem.setProduct(product);
            cartItem.setQuantity(quantity);
            cart.addCartItem(cartItem);
        }

        return cartRepository.save(cart);
    }

    // Get cart for the currently logged-in user
    public Cart getCurrentUserCart(String username) {
        User currentUser = userService.getCurrentUser(username);
        return currentUser.getCart(); // Assuming one-to-one with User
    }

    // Update item quantity in cart
    public CartItems updateCartItemQuantity(Integer cartItemId, int quantity, String username) {
//        CartItems cartItem = cartItemRepository.findById(cartItemId)
//                .orElseThrow(() -> new ResourceNotFoundException("Cart Item"," not found", ""));
        Cart cart = getCurrentUserCart(username);
        // Additional checks (e.g., stock availability) can be added here

        // Find the cart item within the current user's cart
        CartItems cartItem = cart.getCartItems().stream()
                .filter(item -> item.getId().equals(cartItemId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Cart Item"," not found", ""));

        // Additional checks (e.g., stock availability) can be added here

        cartItem.setQuantity(quantity);
        return cartItemRepository.save(cartItem);
//        cartItem.setQuantity(quantity);
//        cartItemRepository.save(cartItem);
    }

    // Remove item from cart
    public void removeItemFromCart(Integer cartItemId, String username) {
        Cart cart = getCurrentUserCart(username); // Get the user's cart

        // Find the cart item to remove within the user's cart
        CartItems cartItemToRemove = cart.getCartItems().stream()
                .filter(item -> item.getId().equals(cartItemId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Cart Item"," not found", ""));

        // Remove the cart item
        cart.removeCartItem(cartItemToRemove);

        cartRepository.save(cart);
    }
}
