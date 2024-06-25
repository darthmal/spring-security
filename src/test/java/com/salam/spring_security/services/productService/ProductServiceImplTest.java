package com.salam.spring_security.services.productService;

import com.salam.spring_security.models.product.Product;
import com.salam.spring_security.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllProduct() {
        // Given
        List<Product> products = new ArrayList<>();
        products.add(new Product(1, "Product 1", "Description 1", 10.0, "Category 1", 10));
        products.add(new Product(2, "Product 2", "Description 2", 20.0, "Category 2", 20));

        when(productRepository.findAll()).thenReturn(products);

        // When
        List<Product> result = productService.getAllProduct();

        // Then
        assertEquals(products, result);
        verify(productRepository, times(1)).findAll();
    }

    @Test
    void getProductById() {
        // Given
        Product product = new Product(1, "Product 1", "Description 1", 10.0, "Category 1", 10);
        when(productRepository.findById(1)).thenReturn(Optional.of(product));

        // When
        Product result = productService.getProductById(1);

        // Then
        assertEquals(product, result);
        verify(productRepository, times(1)).findById(1);
    }

    @Test
    void getProductByIdNotFound() {
        // Given
        when(productRepository.findById(1)).thenReturn(Optional.empty());

        // When / Then
        assertThrows(EntityNotFoundException.class, () -> productService.getProductById(1));
        verify(productRepository, times(1)).findById(1);
    }

    @Test
    void addProduct() {
        // Given
        Product product = new Product(null, "Product 1", "Description 1", 10.0, "Category 1", 10);
        when(productRepository.save(any(Product.class))).thenAnswer(invocation -> {
            Product savedProduct = invocation.getArgument(0);
            savedProduct.setId(1); // Simulate ID generation
            return savedProduct;
        });

        // When
        Product result = productService.addProduct(product);

        // Then
        assertNotNull(result.getId()); // Check that the product now has an ID
        verify(productRepository, times(1)).save(product);
    }

    @Test
    void updateProduct() {
        // Given
        Product existingProduct = new Product(1, "Product 1", "Old Description", 10.0, "Old Category", 10);
        Product updatedProduct = new Product(1, "Updated Product", "New Description", 15.0, "New Category", 20);

        when(productRepository.findById(1)).thenReturn(Optional.of(existingProduct));
        when(productRepository.save(any(Product.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        Product result = productService.updateProduct(1, updatedProduct);

        // Then
        assertEquals("Updated Product", result.getName());
        assertEquals("New Description", result.getDescription());
        assertEquals(15.0, result.getPrice());
        assertEquals("New Category", result.getCategory());
        assertEquals(20, result.getStock());
        verify(productRepository, times(1)).findById(1);
        verify(productRepository, times(1)).save(existingProduct);
    }

    @Test
    void updateProductNotFound() {
        // Given
        Product updatedProduct = new Product(1, "Updated Product", "New Description", 15.0, "New Category", 20);
        when(productRepository.findById(1)).thenReturn(Optional.empty());

        // When / Then
        assertThrows(EntityNotFoundException.class, () -> productService.updateProduct(1, updatedProduct));
        verify(productRepository, times(1)).findById(1);
        verify(productRepository, never()).save(any(Product.class)); // make sure th e save method from updateProduct i s never called
    }

    @Test
    void deleteProduct() {
        // Given
        doNothing().when(productRepository).deleteById(1);

        // When
        productService.deleteProduct(1);

        // Then
        verify(productRepository, times(1)).deleteById(1);
    }
}