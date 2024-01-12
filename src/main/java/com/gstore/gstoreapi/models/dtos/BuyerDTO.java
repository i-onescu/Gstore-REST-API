package com.gstore.gstoreapi.models.dtos;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import org.hibernate.validator.constraints.Range;

@Builder
public record BuyerDTO(@Nullable @Pattern(regexp = "[a-zA-Z]+") String name,
                       @Nullable @Pattern(regexp = "[a-zA-Z]+") String country,
                       @Nullable @Pattern(regexp = ".+@.+\\..+") String email,
                       @Nullable @Range(min = 18, max = 120) Integer age) { }
