package com.order_service.domain;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import com.order_service.ApplicationProperties;
import com.order_service.domain.models.OrderCancelledEvent;
import com.order_service.domain.models.OrderCreatedEvent;
import com.order_service.domain.models.OrderDeliveredEvent;
import com.order_service.domain.models.OrderErrorEvent;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;

import java.nio.charset.StandardCharsets;

@Component
class OrderEventPublisher {
    private final RabbitTemplate rabbitTemplate;
    private final ApplicationProperties properties;
	private ObjectMapper objectMapper;

    OrderEventPublisher(RabbitTemplate rabbitTemplate, ApplicationProperties properties, ObjectMapper objectMapper) {
        this.rabbitTemplate = rabbitTemplate;
        this.properties = properties;
        this.objectMapper = objectMapper;
    }

    public void publish(OrderCreatedEvent event) {
        this.send(properties.newOrdersQueue(), event);
    }

    public void publish(OrderDeliveredEvent event) {
        this.send(properties.deliveredOrdersQueue(), event);
    }

    public void publish(OrderCancelledEvent event) {
        this.send(properties.cancelledOrdersQueue(), event);
    }

    public void publish(OrderErrorEvent event) {
        this.send(properties.errorOrdersQueue(), event);
    }
//
//    private void send(String routingKey, Object payload) {
//        rabbitTemplate.convertAndSend(properties.orderEventsExchange(), routingKey, payload);
//    }
    

    // using this method to send data without typeId header
    private void send(String routingKey, Object payload) {
        try {
            String json = objectMapper.writeValueAsString(payload);
            MessageProperties props = new MessageProperties();
            props.setContentType(MessageProperties.CONTENT_TYPE_JSON);
            Message message = new Message(json.getBytes(StandardCharsets.UTF_8), props);
            rabbitTemplate.send(properties.orderEventsExchange(), routingKey, message);
        } catch (Exception e) {
            throw new RuntimeException("Failed to serialize message", e);
        }
    }
    
}