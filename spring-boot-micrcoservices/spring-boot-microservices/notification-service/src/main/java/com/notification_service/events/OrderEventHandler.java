package com.notification_service.events;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.notification_service.domain.NotificationService;
import com.notification_service.domain.OrderEventEntity;
import com.notification_service.domain.OrderEventRepository;
import com.notification_service.domain.models.OrderCancelledEvent;
import com.notification_service.domain.models.OrderCreatedEvent;
import com.notification_service.domain.models.OrderDeliveredEvent;
import com.notification_service.domain.models.OrderErrorEvent;

@Component
public class OrderEventHandler implements ApplicationListener<ApplicationReadyEvent>{
	
	@Autowired
    private RabbitListenerEndpointRegistry registry;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        registry.start();
    }

	private static final Logger log = LoggerFactory.getLogger(OrderEventHandler.class);

	private final ObjectMapper objectMapper;
	private final NotificationService notificationService;
	private final OrderEventRepository orderEventRepository;

	public OrderEventHandler(ObjectMapper objectMapper, NotificationService notificationService, OrderEventRepository orderEventRepository) {
		this.objectMapper = objectMapper;
		this.notificationService = notificationService;
		this.orderEventRepository = orderEventRepository;
	}


	@RabbitListener(queues = "${notification.new-orders-queue}")
	public void handle(OrderCreatedEvent event) {
		if (orderEventRepository.existsByEventId(event.eventId())) {
			log.warn("Received duplicate OrderCreatedEvent with eventId: {}", event.eventId());
			return;
		}
		log.info("Received a OrderCreatedEvent with orderNumber:{}: ", event.orderNumber());
		notificationService.sendOrderCreatedNotification(event);
		var orderEvent = new OrderEventEntity(event.eventId());
		orderEventRepository.save(orderEvent);
	}

	@RabbitListener(queues = "${notification.delivered-orders-queue}")
	public void handle(OrderDeliveredEvent event) {
		if (orderEventRepository.existsByEventId(event.eventId())) {
			log.warn("Received duplicate OrderDeliveredEvent with eventId: {}", event.eventId());
			return;
		}
		log.info("Received a OrderDeliveredEvent with orderNumber:{}: ", event.orderNumber());
		notificationService.sendOrderDeliveredNotification(event);
		var orderEvent = new OrderEventEntity(event.eventId());
		orderEventRepository.save(orderEvent);
	}

	@RabbitListener(queues = "${notification.cancelled-orders-queue}")
	public void handle(OrderCancelledEvent event) {
		if (orderEventRepository.existsByEventId(event.eventId())) {
			log.warn("Received duplicate OrderCancelledEvent with eventId: {}", event.eventId());
			return;
		}
		notificationService.sendOrderCancelledNotification(event);
		log.info("Received a OrderCancelledEvent with orderNumber:{}: ", event.orderNumber());
		var orderEvent = new OrderEventEntity(event.eventId());
		orderEventRepository.save(orderEvent);
	}

	@RabbitListener(queues = "${notification.error-orders-queue}")
	public void handle(OrderErrorEvent event) {
		if (orderEventRepository.existsByEventId(event.eventId())) {
			log.warn("Received duplicate OrderErrorEvent with eventId: {}", event.eventId());
			return;
		}
		log.info("Received a OrderErrorEvent with orderNumber:{}: ", event.orderNumber());
		notificationService.sendOrderErrorEventNotification(event);
		OrderEventEntity orderEvent = new OrderEventEntity(event.eventId());
		orderEventRepository.save(orderEvent);
	}
}
