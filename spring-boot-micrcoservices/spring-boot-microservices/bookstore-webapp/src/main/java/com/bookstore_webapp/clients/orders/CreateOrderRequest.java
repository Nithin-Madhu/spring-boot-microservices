package com.bookstore_webapp.clients.orders;

import java.io.Serializable;
import java.util.Set;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

public record CreateOrderRequest(@NotEmpty(message = "Items cannot be empty.") @Valid Set<OrderItem> items,
		@Valid Customer customer, @Valid Address deliveryAddress) implements Serializable {
}
