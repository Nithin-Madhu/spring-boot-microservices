// testing rabbit mq connection, use the api end points RabbitMQDemoController
//package com.order_service.web.controllers;
//
//import org.springframework.amqp.rabbit.annotation.RabbitListener;
//import org.springframework.stereotype.Service;
//
//import com.order_service.web.controllers.RabbitMQDemoController.MyPayload;
//
//@Service
//public class RabbitMQListener {
//
//	@RabbitListener(queues = "new-orders")
//	public void handleNewOrder(MyPayload myPayload) {
//		System.out.println("New order : " +myPayload.content());
//	}
//
//	@RabbitListener(queues = "delivered-orders")
//	public void handleDeliveredOrder(MyPayload myPayload) {
//		System.out.println("Delivered order : " +myPayload.content());
//	}
//}
