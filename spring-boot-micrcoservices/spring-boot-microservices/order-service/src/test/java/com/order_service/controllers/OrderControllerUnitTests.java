package com.order_service.controllers;


import static org.junit.jupiter.api.Named.named;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.order_service.domain.OrderService;
import com.order_service.domain.SecurityService;
import com.order_service.domain.models.CreateOrderRequest;
import com.order_service.testData.TestDataFactory;

//@WebMvcTest(OrderController.class)
class OrderControllerUnitTests {
    @MockitoBean
    private OrderService orderService;

    @MockitoBean
    private SecurityService securityService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;
   

    @BeforeEach
    void setUp() {
        given(securityService.getLoginUserName()).willReturn("Nithin");
    }

    @ParameterizedTest(name = "[{index}]-{0}")
    @MethodSource("createOrderRequestProvider")
    @WithMockUser
    void shouldReturnBadRequestWhenOrderPayloadIsInvalid(CreateOrderRequest request) throws Exception {
        given(orderService.createOrder(eq("Nithin"), any(CreateOrderRequest.class)))
                .willReturn(null);

        mockMvc.perform(post("/api/orders")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    static Stream<Arguments> createOrderRequestProvider() {
        return Stream.of(
                arguments(named("Order with Invalid Customer", TestDataFactory.createOrderRequestWithInvalidCustomer())),
                arguments(named("Order with Invalid Delivery Address", TestDataFactory.createOrderRequestWithInvalidDeliveryAddress())),
                arguments(named("Order with No Items", TestDataFactory.createOrderRequestWithNoItems())));
    }
}