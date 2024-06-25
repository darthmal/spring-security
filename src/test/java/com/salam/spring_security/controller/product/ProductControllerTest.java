package com.salam.spring_security.controller.product;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.salam.spring_security.config.JwtService;
import com.salam.spring_security.models.product.Product;
import com.salam.spring_security.services.productService.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context; // Inject WebApplicationContext

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @MockBean
    private JwtService jwtService;

    @MockBean
    private ProductService productService;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    void getAllProduct() throws Exception {
        // Given
        Product product1 = new Product(1, "Product 1", "Description 1", 10.0, "Category 1", 10);
        Product product2 = new Product(2, "Product 2", "Description 2", 20.0, "Category 2", 20);
        List<Product> products = Arrays.asList(product1, product2);

        when(productService.getAllProduct()).thenReturn(products);

        // When / Then
        mockMvc.perform(get("/products")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(product1.getId())))
                .andExpect(jsonPath("$[1].name", is(product2.getName())));
    }


    @Test
    void getProductById() throws Exception {
        // Given
        Product product = new Product(1, "Product 1", "Description 1", 10.0, "Category 1", 10);

        when(productService.getProductById(1)).thenReturn(product);

        // When / Then
        mockMvc.perform(get("/products/{id}", product.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(product.getId())))
                .andExpect(jsonPath("$.name", is(product.getName())))
                .andExpect(jsonPath("$.description", is(product.getDescription())))
                .andExpect(jsonPath("$.price", is(product.getPrice())))
                .andExpect(jsonPath("$.category", is(product.getCategory())))
                .andExpect(jsonPath("$.stock", is(product.getStock())));
    }

    @Test
    void addProduct() throws Exception {
        // Given
        Product product = new Product(null, "Product 1", "Description 1", 10.0, "Category 1", 10);
        when(productService.addProduct(any(Product.class))).thenAnswer(invocation -> {
            Product savedProduct = invocation.getArgument(0);
            savedProduct.setId(1);
            return savedProduct;
        });

        // When / Then
        mockMvc.perform(post("/products")
                        .content(objectMapper.writeValueAsString(product))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)));
    }

    @Test
    void updateProduct() throws Exception {
        // Given
        Product product = new Product(1, "Updated Product", "Description 1", 10.0, "Category 1", 10);
        when(productService.updateProduct(anyInt(), any(Product.class))).thenReturn(product);

        // When / Then
        mockMvc.perform(put("/products/{id}", product.getId())
                        .content(objectMapper.writeValueAsString(product))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name", is(product.getName())));
    }

    @Test
    void deleteProductById() throws Exception {
        // Given
        // You don't need to mock anything for deleteProduct as it returns void
        // When / Then
        mockMvc.perform(delete("/products/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted())
                .andExpect(content().string("Deleted Successfully"));
    }
}