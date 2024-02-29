package com.gstore.gstoreapi.models.dtos;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record QuantityDTO(@NotNull Long productId,
                          @NotNull Integer quantity) {}
