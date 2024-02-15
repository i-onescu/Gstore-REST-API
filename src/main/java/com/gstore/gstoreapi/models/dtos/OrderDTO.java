package com.gstore.gstoreapi.models.dtos;

import com.gstore.gstoreapi.models.constants.OrderStatus;
import com.gstore.gstoreapi.models.entities.Quantity;
import jakarta.annotation.Nullable;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;

@Builder
public record OrderDTO(@Nullable String orderNumber,
                       @Nullable Double price,
                       @Nullable OrderStatus status,
                       @Nullable Long buyerId,
                       @Nullable Set<QuantityDto> orderQuantities,
                       @Nullable LocalDateTime placedDateTime) { }
