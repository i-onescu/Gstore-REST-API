package com.gstore.gstoreapi.models.dtos;

import jakarta.annotation.Nullable;
import lombok.Builder;

@Builder
public record QuantityDto(@Nullable Long productId,
                          @Nullable Integer quantity) {}
