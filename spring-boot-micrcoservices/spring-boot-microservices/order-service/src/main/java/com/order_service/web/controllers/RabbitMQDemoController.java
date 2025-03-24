package com.order_service.web.controllers;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.order_service.ApplicationProperties;


@RestController
public class RabbitMQDemoController {

	private final RabbitTemplate rabbitTemplate;
	private final ApplicationProperties applicationProperties;

	public RabbitMQDemoController(RabbitTemplate rabbitTemplate, ApplicationProperties applicationProperties) {
		super();
		this.rabbitTemplate = rabbitTemplate;
		this.applicationProperties = applicationProperties;
	}

	public record MyMessage(String routingKey, MyPayload payload){
	}

	public record MyPayload (String content){
	}
	
	@PostMapping("/send")
	public void sendMessage(@RequestBody MyMessage myMessage) {
	    System.out.println("myMessage : " + myMessage);
		
		rabbitTemplate.convertAndSend(
				applicationProperties.orderEventsExchange(),
				myMessage.routingKey(),
				myMessage.payload()
				);
	}


}
