package com.gstore.gstoreapi.models.dtos;

import com.gstore.gstoreapi.models.constants.OrderStatus;
import jakarta.annotation.Nullable;
import lombok.Builder;

import java.util.List;
import java.util.Set;

@Builder
public record OrderDTO(@Nullable String orderNumber,
                       @Nullable Long buyerId,
                       @Nullable OrderStatus status,

                       //Because an order may have more products listed i created a list of product ids
                       //meant to represent a list of products that may be on a particular order
                       @Nullable List<Long> productIds) { }

