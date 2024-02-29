package com.gstore.gstoreapi.models.dtos;

import com.gstore.gstoreapi.enums.OrderStatus;
import jakarta.annotation.Nullable;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.Set;

@Builder
public record OrderDTO(@Nullable String orderNumber,
                       @Nullable Double price,
                       @Nullable OrderStatus status,
                       @Nullable Long buyerId,
                       @Nullable Set<QuantityDTO> orderQuantities,
                       @Nullable LocalDateTime placedDateTime) { }
