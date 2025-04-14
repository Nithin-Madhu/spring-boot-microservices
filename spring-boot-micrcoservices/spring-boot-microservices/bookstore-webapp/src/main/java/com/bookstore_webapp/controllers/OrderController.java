package com.bookstore_webapp.controllers;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bookstore_webapp.clients.orders.CreateOrderRequest;
import com.bookstore_webapp.clients.orders.OrderConfirmationDTO;
import com.bookstore_webapp.clients.orders.OrderDTO;
import com.bookstore_webapp.clients.orders.OrderServiceClient;
import com.bookstore_webapp.clients.orders.OrderSummary;

import jakarta.validation.Valid;

@Controller
public class OrderController {
	private static final Logger log = LoggerFactory.getLogger(OrderController.class);
	
    private final OrderServiceClient orderServiceClient;

    OrderController(OrderServiceClient orderServiceClient){
        this.orderServiceClient = orderServiceClient;
    }

	@GetMapping("/cart")
	String cart() {
		return "cart";
	}

	@GetMapping("/orders/{orderNumber}")
	String showOrderDetails(@PathVariable String orderNumber, Model model) {
		model.addAttribute("orderNumber", orderNumber);
		return "order_details";
	}

	@GetMapping("/orders")
	String showOrders() {
		return "orders";
	}
	
    @GetMapping("/api/orders")
    @ResponseBody
    List<OrderSummary> getOrders() {
        log.info("Fetching orders");
        return orderServiceClient.getOrders();//getHeaders());
    }
    
    @GetMapping("/api/orders/{orderNumber}")
    @ResponseBody
    OrderDTO getOrder(@PathVariable String orderNumber) {
        log.info("Fetching order details for orderNumber: {}", orderNumber);
        return orderServiceClient.getOrder(orderNumber);
    }
    
    @PostMapping("/api/orders")
    @ResponseBody
    OrderConfirmationDTO createOrder(@Valid @RequestBody CreateOrderRequest orderRequest) {
        log.info("Creating order: {}", orderRequest);
        return orderServiceClient.createOrder(orderRequest);
    }
}
