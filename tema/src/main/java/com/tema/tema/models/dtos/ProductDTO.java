package com.tema.tema.models.dtos;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import org.hibernate.validator.constraints.Range;

@Builder
public record ProductDTO(@Nullable @Pattern(regexp = "[a-zA-Z]+") String name,
                         @Nullable @Pattern(regexp = "[0-9a-zA-Z]+") String manufacturer,
                         @Nullable @Pattern(regexp = "[a-zA-Z]+") String country,
                         @Nullable @Range(min = 1, max = 50000) Double price,
                         @Nullable @Range(min = 1, max = 10) Integer rating,
                         @Nullable Long sellerId) { }
