package com.order_service.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.order_service.ApplicationProperties;

@Configuration
public class RabbitMQConfig {

	private final ApplicationProperties properties;

	public RabbitMQConfig(ApplicationProperties properties) {
		System.out.println("RabbitMQConfig initialized with: " + properties.orderEventsExchange());
		this.properties = properties;
	}

	@Bean
	DirectExchange orderEventsExchange() {
		return new DirectExchange(properties.orderEventsExchange(), true, false);
	}

	@Bean
	Queue newOrdersQueue() {
		return new Queue(properties.newOrdersQueue(), true, false, false);
	}

	@Bean
	Queue deliveredOrdersQueue() {
		return new Queue(properties.deliveredOrdersQueue());
	}

	@Bean
	Queue cancelledOrdersQueue() {
		return new Queue(properties.cancelledOrdersQueue());
	}

	@Bean
	Queue errorOrdersQueue() {
		return new Queue(properties.errorOrdersQueue());
	}

	@Bean
	Binding newOrdersBinding(DirectExchange orderEventsExchange, Queue newOrdersQueue) {
		System.out.println("newOrdersBinding : " + properties.newOrdersQueue());
		return BindingBuilder.bind(newOrdersQueue).to(orderEventsExchange).with(properties.newOrdersQueue());
	}

	@Bean
	Binding deliveredOrdersBinding(DirectExchange orderEventsExchange, Queue deliveredOrdersQueue) {
		System.out.println("deliveredOrdersBinding : " + properties.deliveredOrdersQueue());
		return BindingBuilder.bind(deliveredOrdersQueue).to(orderEventsExchange)
				.with(properties.deliveredOrdersQueue());
	}

	@Bean
	Binding cancelledOrdersBinding(DirectExchange orderEventsExchange, Queue cancelledOrdersQueue) {
		System.out.println("cancelledOrdersBinding : " + properties.cancelledOrdersQueue());
		return BindingBuilder.bind(cancelledOrdersQueue).to(orderEventsExchange)
				.with(properties.cancelledOrdersQueue());
	}

	@Bean
	Binding errorOrdersBinding(DirectExchange orderEventsExchange, Queue errorOrdersQueue) {
		System.out.println("errorOrdersBinding : " + properties.errorOrdersQueue());
		return BindingBuilder.bind(errorOrdersQueue).to(orderEventsExchange).with(properties.errorOrdersQueue());
	}

	@Bean
	RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
	    RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
	    rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
	    return rabbitTemplate;
	}

	@Bean
	Jackson2JsonMessageConverter jacksonConverter(ObjectMapper mapper) {
		return new Jackson2JsonMessageConverter(mapper);
	}
}
