package com.tema.tema.models.dtos;

import jakarta.annotation.Nullable;
import lombok.Builder;

import java.util.Set;


@Builder
public record OrderDTO(@Nullable String orderNumber,
                       @Nullable Long buyerId,

                       //Because an order may have more products listed i created a list of product ids
                       //meant to represent a list of products that may be on a particular order
                       @Nullable Set<Long> productIds) { }
