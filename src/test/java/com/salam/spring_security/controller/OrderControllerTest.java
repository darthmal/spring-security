package com.salam.spring_security.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.salam.spring_security.config.JwtService;
import com.salam.spring_security.models.order.Order;
import com.salam.spring_security.models.order.OrderStatus;
import com.salam.spring_security.services.order.OrderService;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrderController.class)
@EnableWebSecurity
class OrderControllerTest {

    @Autowired
    private WebApplicationContext context; // Inject WebApplicationContext

    @Autowired
    private WebApplicationContext webApplicationContext;


    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Autowired
    private MockMvc mockMvc;

    @MockBean // Mock JwtService
    private JwtService jwtService;

    @MockBean
    private OrderService orderService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(username = "testuser", roles = "USER")
    void getAllUsersOrder() throws Exception {
        // Given
        Order order1 = Order.builder().id(1).orderStatus(OrderStatus.PENDING).build();
        Order order2 = Order.builder().id(2).orderStatus(OrderStatus.PENDING).build();
        List<Order> orders = Arrays.asList(order1, order2);

        when(orderService.getAllOrdersForUser("testuser")).thenReturn(orders);

        // When / Then
        mockMvc.perform(get("/orders")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(order1.getId())))
                .andExpect(jsonPath("$[1].id", is(order2.getId())));

    }


    @Test
    @WithMockUser(username = "testuser", roles = "USER")
    void getOrderbyId() throws Exception {
        Order order = Order.builder()
                .id(1)
                .totalAmount(100.00)
                .orderDate(LocalDateTime.now())
                .orderStatus(OrderStatus.PENDING)
                .build();

        when(orderService.getOrderById(1, "testuser")).thenReturn(order);

        mockMvc.perform(get("/orders/{id}", order.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(order.getId())))
                .andExpect(jsonPath("$.totalAmount", is(order.getTotalAmount())))
                .andExpect(jsonPath("$.orderStatus", is(order.getOrderStatus().name())));

    }

    @Test
    @WithMockUser(username = "testuser", roles = "USER")
    void newOrder() throws Exception {


        Order order = Order.builder()
                .id(1)
                .totalAmount(100.00)
                .orderDate(LocalDateTime.now())
                .orderStatus(OrderStatus.PENDING)
                .build();

        when(orderService.createOrder("testuser")).thenReturn(order);

        mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(order.getId())))
                .andExpect(jsonPath("$.totalAmount", is(order.getTotalAmount())))
                .andExpect(jsonPath("$.orderStatus", is(order.getOrderStatus().name())));
    }

}